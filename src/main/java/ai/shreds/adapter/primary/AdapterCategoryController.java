package ai.shreds.adapter.primary;

import ai.shreds.application.ports.ApplicationInputPortCategoryService;
import ai.shreds.shared.dtos.SharedCreateCategoryRequestDTO;
import ai.shreds.shared.dtos.SharedCategoryResponseDTO;
import ai.shreds.shared.dtos.SharedCategoryTreeResponseDTO;
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
 * REST controller for managing category operations.
 * Provides endpoints for creating categories and retrieving the category tree.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Management", description = "APIs for category lifecycle management")
public class AdapterCategoryController {

    private final ApplicationInputPortCategoryService categoryService;

    @Operation(summary = "Create a new category", description = "Creates a new category and computes its hierarchical data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully",
                    content = @Content(schema = @Schema(implementation = SharedCategoryResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category data supplied"),
            @ApiResponse(responseCode = "404", description = "Parent category not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedCategoryResponseDTO> createCategory(
            @Valid @RequestBody SharedCreateCategoryRequestDTO request) {
        log.debug("REST request to create Category: {}", request);
        SharedCategoryResponseDTO response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get full category tree", description = "Retrieves the entire active category hierarchy as a nested structure")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category tree fetched successfully",
                    content = @Content(schema = @Schema(implementation = SharedCategoryTreeResponseDTO.class)))
    })
    @GetMapping(value = "/tree", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SharedCategoryTreeResponseDTO> getCategoryTree() {
        log.debug("REST request to get Category tree");
        SharedCategoryTreeResponseDTO response = categoryService.getCategoryTree();
        return ResponseEntity.ok(response);
    }
}
