package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortProductService;
import ai.shreds.shared.dtos.SharedCreateProductRequestDTO;
import ai.shreds.shared.dtos.SharedUpdateProductRequestDTO;
import ai.shreds.shared.dtos.SharedProductResponseDTO;
import ai.shreds.shared.dtos.SharedPagedProductResponseDTO;
import ai.shreds.shared.value_objects.SharedProductSearchParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing product operations.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Management", description = "APIs for product lifecycle management")
public class AdapterProductController {

    private final ApplicationInputPortProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a new product with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully",
                    content = @Content(schema = @Schema(implementation = SharedProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data supplied"),
            @ApiResponse(responseCode = "404", description = "Referenced category not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedProductResponseDTO> createProduct(
            @Valid @RequestBody SharedCreateProductRequestDTO request) {
        log.debug("REST request to create Product: {}", request);
        SharedProductResponseDTO response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get a product by ID", description = "Retrieves a product by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found and returned", 
                    content = @Content(schema = @Schema(implementation = SharedProductResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedProductResponseDTO> getProduct(
            @Parameter(description = "ID of the product to retrieve", required = true)
            @PathVariable UUID id) {
        log.debug("REST request to get Product : {}", id);
        SharedProductResponseDTO response = productService.getProduct(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update an existing product", description = "Updates a product with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully", 
                    content = @Content(schema = @Schema(implementation = SharedProductResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid product data supplied"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - outdated version")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedProductResponseDTO> updateProduct(
            @Parameter(description = "ID of the product to update", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody SharedUpdateProductRequestDTO request) {
        log.debug("REST request to update Product : {}, {}", id, request);
        SharedProductResponseDTO response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete", required = true)
            @PathVariable UUID id) {
        log.debug("REST request to delete Product : {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List products", description = "Returns a paginated list of products with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products successfully retrieved", 
                    content = @Content(schema = @Schema(implementation = SharedPagedProductResponseDTO.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedPagedProductResponseDTO> listProducts(
            @Valid SharedProductSearchParams searchParams,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("REST request to get a page of Products with params: {}", searchParams);
        SharedPagedProductResponseDTO response = productService.listProducts(searchParams, pageable);
        return ResponseEntity.ok(response);
    }
}
