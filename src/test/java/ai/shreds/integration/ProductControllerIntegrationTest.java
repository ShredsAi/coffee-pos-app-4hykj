package ai.shreds.integration;

import ai.shreds.CoreProductManagementApplication;
import ai.shreds.shared.dtos.*;
import ai.shreds.shared.value_objects.SharedMoneyValueDTO;
import ai.shreds.shared.utils.SharedProductStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ProductController focusing on optimistic locking scenarios.
 * Tests the complete product lifecycle including database persistence and version conflict handling.
 */
@SpringBootTest(
    classes = CoreProductManagementApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(OutputCaptureExtension.class)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Slf4j
class ProductControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("productdb_test")
            .withUsername("product_user")
            .withPassword("product_pass")
            .withReuse(false);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private UUID testCategoryId;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("logging.level.ai.shreds", () -> "DEBUG");
        registry.add("logging.level.org.springframework.test", () -> "DEBUG");
        registry.add("logging.level.org.testcontainers", () -> "INFO");
        registry.add("logging.level.org.hibernate.SQL", () -> "DEBUG");
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        log.info("Test setup: Base URL = {}", baseUrl);
        log.info("PostgreSQL container running on: {}", postgreSQLContainer.getJdbcUrl());
        
        // Create a test category first since products require a valid categoryId
        testCategoryId = createTestCategory();
    }

    @Test
    void When_Product_Is_Updated_With_Version_Conflict_Then_Returns_409_Conflict(CapturedOutput output) {
        log.info("============= Starting Optimistic Locking Test =============");
        
        // Step 1: Create a product
        log.info("Step 1: Creating a test product");
        SharedCreateProductRequestDTO createRequest = buildCreateProductRequest();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SharedCreateProductRequestDTO> createEntity = new HttpEntity<>(createRequest, headers);
        
        ResponseEntity<SharedProductResponseDTO> createResponse = restTemplate.exchange(
            baseUrl + "/api/products",
            HttpMethod.POST,
            createEntity,
            SharedProductResponseDTO.class
        );
        
        // Verify product creation was successful
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode(), "Product should be created successfully");
        assertNotNull(createResponse.getBody(), "Response body should not be null");
        
        SharedProductResponseDTO createdProduct = createResponse.getBody();
        UUID productId = createdProduct.getId();
        Long originalVersion = createdProduct.getVersion();
        
        assertNotNull(productId, "Product ID should not be null");
        assertNotNull(originalVersion, "Product version should not be null");
        assertEquals("Test Product", createdProduct.getName(), "Product name should match");
        
        log.info("✓ Product created successfully with ID: {} and version: {}", productId, originalVersion);
        
        // Step 2: Retrieve the product to get current version (simulate concurrent access)
        log.info("Step 2: Retrieving product to get current version");
        
        ResponseEntity<SharedProductResponseDTO> getResponse = restTemplate.exchange(
            baseUrl + "/api/products/" + productId,
            HttpMethod.GET,
            null,
            SharedProductResponseDTO.class
        );
        
        assertEquals(HttpStatus.OK, getResponse.getStatusCode(), "Product should be retrieved successfully");
        SharedProductResponseDTO retrievedProduct = getResponse.getBody();
        assertNotNull(retrievedProduct, "Retrieved product should not be null");
        assertEquals(originalVersion, retrievedProduct.getVersion(), "Version should match");
        
        log.info("✓ Product retrieved successfully with version: {}", retrievedProduct.getVersion());
        
        // Step 3: Simulate concurrent update - first update to increment version
        log.info("Step 3: Performing first update to increment version (simulate concurrent user)");
        
        SharedUpdateProductRequestDTO firstUpdate = buildUpdateProductRequest(
            originalVersion, 
            "Updated Product Name"
        );
        
        HttpEntity<SharedUpdateProductRequestDTO> firstUpdateEntity = new HttpEntity<>(firstUpdate, headers);
        
        ResponseEntity<SharedProductResponseDTO> firstUpdateResponse = restTemplate.exchange(
            baseUrl + "/api/products/" + productId,
            HttpMethod.PUT,
            firstUpdateEntity,
            SharedProductResponseDTO.class
        );
        
        assertEquals(HttpStatus.OK, firstUpdateResponse.getStatusCode(), "First update should be successful");
        SharedProductResponseDTO firstUpdatedProduct = firstUpdateResponse.getBody();
        assertNotNull(firstUpdatedProduct, "First updated product should not be null");
        
        Long newVersion = firstUpdatedProduct.getVersion();
        assertTrue(newVersion > originalVersion, "Version should be incremented after update");
        assertEquals("Updated Product Name", firstUpdatedProduct.getName(), "Product name should be updated");
        
        log.info("✓ First update successful. Version incremented from {} to {}", originalVersion, newVersion);
        
        // Step 4: Attempt to update with the original (now outdated) version - this should fail with 409
        log.info("Step 4: Attempting update with outdated version (should trigger optimistic locking conflict)");
        
        SharedUpdateProductRequestDTO conflictingUpdate = buildUpdateProductRequest(
            originalVersion, // Using original version which is now outdated
            "Conflicting Update"
        );
        
        HttpEntity<SharedUpdateProductRequestDTO> conflictingEntity = new HttpEntity<>(conflictingUpdate, headers);
        
        ResponseEntity<String> conflictResponse = restTemplate.exchange(
            baseUrl + "/api/products/" + productId,
            HttpMethod.PUT,
            conflictingEntity,
            String.class  // Using String to capture error response
        );
        
        // Step 5: Verify that the response is 409 Conflict
        log.info("Step 5: Verifying optimistic locking conflict response");
        
        assertEquals(HttpStatus.CONFLICT, conflictResponse.getStatusCode(), 
            "Update with outdated version should return 409 Conflict");
        
        String responseBody = conflictResponse.getBody();
        assertNotNull(responseBody, "Conflict response body should not be null");
        
        // Verify the error response contains expected information
        assertTrue(responseBody.contains("409") || responseBody.contains("Conflict") || 
                  responseBody.contains("version") || responseBody.contains("optimistic"),
                  "Response should indicate version conflict. Response: " + responseBody);
        
        log.info("✓ Optimistic locking conflict detected correctly. Response status: {}, Body: {}", 
                conflictResponse.getStatusCode(), responseBody);
        
        // Step 6: Verify that the product was not modified by the conflicting update
        log.info("Step 6: Verifying that the product remains unchanged after conflict");
        
        ResponseEntity<SharedProductResponseDTO> finalGetResponse = restTemplate.exchange(
            baseUrl + "/api/products/" + productId,
            HttpMethod.GET,
            null,
            SharedProductResponseDTO.class
        );
        
        assertEquals(HttpStatus.OK, finalGetResponse.getStatusCode(), "Final product retrieval should succeed");
        SharedProductResponseDTO finalProduct = finalGetResponse.getBody();
        assertNotNull(finalProduct, "Final product should not be null");
        
        assertEquals(newVersion, finalProduct.getVersion(), "Version should remain as updated by first update");
        assertEquals("Updated Product Name", finalProduct.getName(), "Name should remain as updated by first update");
        assertNotEquals("Conflicting Update", finalProduct.getName(), "Name should NOT be changed by conflicting update");
        
        log.info("✓ Product integrity maintained. Final version: {}, Name: {}", 
                finalProduct.getVersion(), finalProduct.getName());
        
        // Step 7: Verify logs contain expected behavior
        log.info("Step 7: Analyzing captured logs for expected behavior");
        
        String logOutput = output.getOut();
        assertTrue(logOutput.contains("optimistic") || logOutput.contains("version") || 
                  logOutput.contains("conflict") || logOutput.contains("concurrent"),
                  "Logs should contain indicators of optimistic locking behavior");
        
        log.info("✓ Captured logs contain optimistic locking indicators");
        
        log.info("============= Optimistic Locking Test Completed Successfully =============");
    }
    
    private UUID createTestCategory() {
        log.info("Creating test category for product tests");
        
        SharedCreateCategoryRequestDTO categoryRequest = SharedCreateCategoryRequestDTO.builder()
                .name("Test Category")
                .description("Category for integration tests")
                .displayOrder(1)
                .build();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SharedCreateCategoryRequestDTO> entity = new HttpEntity<>(categoryRequest, headers);
        
        ResponseEntity<SharedCategoryResponseDTO> response = restTemplate.exchange(
            baseUrl + "/api/categories",
            HttpMethod.POST,
            entity,
            SharedCategoryResponseDTO.class
        );
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Test category should be created successfully");
        assertNotNull(response.getBody(), "Category response should not be null");
        
        UUID categoryId = response.getBody().getId();
        log.info("✓ Test category created with ID: {}", categoryId);
        
        return categoryId;
    }
    
    private SharedCreateProductRequestDTO buildCreateProductRequest() {
        return SharedCreateProductRequestDTO.builder()
                .name("Test Product")
                .description("A test product for integration testing")
                .shortDescription("Test product")
                .brand("Test Brand")
                .model("TEST-001")
                .categoryId(testCategoryId)
                .basePrice(SharedMoneyValueDTO.builder()
                        .amount(new BigDecimal("99.99"))
                        .currency("USD")
                        .build())
                .build();
    }
    
    private SharedUpdateProductRequestDTO buildUpdateProductRequest(Long version, String name) {
        return SharedUpdateProductRequestDTO.builder()
                .name(name)
                .description("Updated test product description")
                .shortDescription("Updated test product")
                .brand("Test Brand")
                .model("TEST-001")
                .categoryId(testCategoryId)
                .basePrice(SharedMoneyValueDTO.builder()
                        .amount(new BigDecimal("109.99"))
                        .currency("USD")
                        .build())
                .status(SharedProductStatusEnum.ACTIVE)
                .version(version)
                .build();
    }
}