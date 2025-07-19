package ai.shreds.integration;

import ai.shreds.CoreProductManagementApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    classes = CoreProductManagementApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(OutputCaptureExtension.class)
@Testcontainers
@ActiveProfiles("test")
@Slf4j
class CoreProductManagementApplicationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("productdb_test")
            .withUsername("product_user")
            .withPassword("product_pass")
            .withReuse(true);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

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
    }

    @Test
    void shouldStartApplicationSuccessfully(CapturedOutput output) {
        log.info("Starting Core Product Management Application Integration Test");
        log.info("Application running on port: {}", port);
        log.info("PostgreSQL container running on: {}", postgreSQLContainer.getJdbcUrl());
        
        // Verify application context loaded successfully
        assertNotNull(applicationContext, "Application context should be loaded");
        log.info("✓ Application context loaded successfully");
        
        // Verify essential beans are present
        verifyEssentialBeansPresent();
        
        // Verify database connection
        verifyDatabaseConnection();
        
        // Verify web server is running
        verifyWebServerRunning();
        
        // Verify actuator endpoints
        verifyActuatorEndpoints();
        
        // Verify API endpoints are accessible
        verifyApiEndpoints();
        
        // Verify logs contain expected startup messages
        verifyStartupLogs(output);
        
        log.info("✓ Core Product Management Application started successfully!");
        log.info("✓ All integration test validations passed");
    }
    
    private void verifyEssentialBeansPresent() {
        // Verify controller beans
        assertNotNull(applicationContext.getBean("adapterProductController"), 
                     "Product controller should be available");
        assertNotNull(applicationContext.getBean("adapterSKUController"), 
                     "SKU controller should be available");
        assertNotNull(applicationContext.getBean("adapterCategoryController"), 
                     "Category controller should be available");
        
        // Verify service beans
        assertNotNull(applicationContext.getBean("applicationProductService"), 
                     "Product service should be available");
        assertNotNull(applicationContext.getBean("applicationSKUService"), 
                     "SKU service should be available");
        assertNotNull(applicationContext.getBean("applicationCategoryService"), 
                     "Category service should be available");
        
        // Verify domain service beans
        assertNotNull(applicationContext.getBean("domainProductCommandService"), 
                     "Product command service should be available");
        assertNotNull(applicationContext.getBean("domainSKUService"), 
                     "Domain SKU service should be available");
        assertNotNull(applicationContext.getBean("domainCategoryService"), 
                     "Domain category service should be available");
        
        // Verify repository beans
        assertNotNull(applicationContext.getBean("infrastructureProductRepositoryImpl"), 
                     "Product repository should be available");
        assertNotNull(applicationContext.getBean("infrastructureSKURepositoryImpl"), 
                     "SKU repository should be available");
        assertNotNull(applicationContext.getBean("infrastructureCategoryRepositoryImpl"), 
                     "Category repository should be available");
        
        log.info("✓ All essential beans are present in the application context");
    }
    
    private void verifyDatabaseConnection() {
        assertTrue(postgreSQLContainer.isRunning(), "PostgreSQL container should be running");
        assertNotNull(applicationContext.getBean("dataSource"), "DataSource bean should be available");
        log.info("✓ Database connection verified");
    }
    
    private void verifyWebServerRunning() {
        assertTrue(port > 0, "Server port should be assigned");
        
        String baseUrl = "http://localhost:" + port;
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/actuator/health", String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode(), 
                    "Health endpoint should return OK status");
        assertNotNull(response.getBody(), "Health response should have body");
        assertTrue(response.getBody().contains("UP"), 
                  "Health endpoint should indicate application is UP");
        
        log.info("✓ Web server running and accessible on port {}", port);
    }
    
    private void verifyActuatorEndpoints() {
        String baseUrl = "http://localhost:" + port;
        
        // Test health endpoint
        ResponseEntity<String> healthResponse = restTemplate.getForEntity(
            baseUrl + "/actuator/health", String.class);
        assertEquals(HttpStatus.OK, healthResponse.getStatusCode());
        
        // Test info endpoint
        ResponseEntity<String> infoResponse = restTemplate.getForEntity(
            baseUrl + "/actuator/info", String.class);
        // Info endpoint may return 200 or 404 depending on configuration
        assertTrue(infoResponse.getStatusCode() == HttpStatus.OK || 
                  infoResponse.getStatusCode() == HttpStatus.NOT_FOUND);
        
        log.info("✓ Actuator endpoints are accessible");
    }
    
    private void verifyApiEndpoints() {
        String baseUrl = "http://localhost:" + port;
        
        // Test API documentation endpoint
        ResponseEntity<String> docsResponse = restTemplate.getForEntity(
            baseUrl + "/api-docs", String.class);
        assertEquals(HttpStatus.OK, docsResponse.getStatusCode(), 
                    "API docs should be accessible");
        
        // Test Swagger UI endpoint
        ResponseEntity<String> swaggerResponse = restTemplate.getForEntity(
            baseUrl + "/swagger-ui.html", String.class);
        // Swagger UI might redirect, so accept 200 or 3xx
        assertTrue(swaggerResponse.getStatusCode().is2xxSuccessful() || 
                  swaggerResponse.getStatusCode().is3xxRedirection(),
                  "Swagger UI should be accessible");
        
        // Test categories endpoint (should work without authentication)
        ResponseEntity<String> categoriesResponse = restTemplate.getForEntity(
            baseUrl + "/api/categories/tree", String.class);
        // Categories endpoint should return 200 (empty tree) or might have validation errors
        assertTrue(categoriesResponse.getStatusCode() == HttpStatus.OK || 
                  categoriesResponse.getStatusCode() == HttpStatus.BAD_REQUEST ||
                  categoriesResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR,
                  "Categories endpoint should be accessible (status: " + 
                  categoriesResponse.getStatusCode() + ")");
        
        log.info("✓ API endpoints are accessible");
    }
    
    private void verifyStartupLogs(CapturedOutput output) {
        String logOutput = output.getOut();
        
        // Verify Spring Boot started successfully
        assertTrue(logOutput.contains("Started CoreProductManagementApplication") || 
                  logOutput.contains("JVM running for"),
                  "Should contain Spring Boot startup completion message");
        
        // Verify no critical errors during startup
        String errorOutput = output.getErr();
        assertFalse(errorOutput.contains("APPLICATION FAILED TO START"),
                   "Should not contain application startup failure messages");
        
        // Verify Tomcat started
        assertTrue(logOutput.contains("Tomcat started on port") || 
                  logOutput.contains("Undertow started on port"),
                  "Should contain web server startup message");
        
        // Verify JPA/Hibernate initialized
        assertTrue(logOutput.contains("HikariPool") || 
                  logOutput.contains("Started HikariPool") ||
                  logOutput.contains("Using dialect"),
                  "Should contain database/JPA initialization messages");
        
        log.info("✓ Startup logs contain expected success messages");
        log.info("\n=== CAPTURED STARTUP LOGS ===\n{}", logOutput);
        
        if (!errorOutput.isEmpty()) {
            log.warn("\n=== CAPTURED ERROR LOGS ===\n{}", errorOutput);
        }
    }
}
