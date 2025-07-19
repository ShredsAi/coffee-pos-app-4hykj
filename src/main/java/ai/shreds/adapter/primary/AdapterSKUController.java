package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortSKUService;
import ai.shreds.shared.dtos.SharedCreateSKURequestDTO;
import ai.shreds.shared.dtos.SharedSKUResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Stock Keeping Unit (SKU) operations.
 * Provides endpoints for creating and managing SKUs related to products.
 */
@RestController
@RequestMapping("/api/skus")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "SKU Management", description = "APIs for SKU management operations")
public class AdapterSKUController {

    private final ApplicationInputPortSKUService skuService;

    @Operation(summary = "Create a new SKU", description = "Creates a new Stock Keeping Unit (SKU) with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SKU created successfully",
                    content = @Content(schema = @Schema(implementation = SharedSKUResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid SKU data supplied"),
            @ApiResponse(responseCode = "404", description = "Referenced product not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedSKUResponseDTO> createSKU(
            @Valid @RequestBody SharedCreateSKURequestDTO request) {
        log.debug("REST request to create SKU for product {}: {}", request.getProductId(), request);
        SharedSKUResponseDTO response = skuService.createSKU(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
