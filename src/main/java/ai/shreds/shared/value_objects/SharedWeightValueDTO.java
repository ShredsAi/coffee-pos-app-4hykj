package ai.shreds.shared.value_objects;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedWeightValueDTO {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Weight must be positive")
    private BigDecimal value;

    @NotBlank
    private String unit;
}