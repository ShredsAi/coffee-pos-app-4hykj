package ai.shreds.shared.value_objects;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedDimensionsValueDTO {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Length must be positive")
    private BigDecimal length;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Width must be positive")
    private BigDecimal width;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Height must be positive")
    private BigDecimal height;

    @NotBlank
    private String unit;
}