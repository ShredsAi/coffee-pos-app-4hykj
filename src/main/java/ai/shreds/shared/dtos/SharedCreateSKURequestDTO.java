package ai.shreds.shared.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.*;
import java.util.UUID;
import java.util.Map;

import ai.shreds.shared.value_objects.SharedMoneyValueDTO;
import ai.shreds.shared.value_objects.SharedWeightValueDTO;
import ai.shreds.shared.value_objects.SharedDimensionsValueDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCreateSKURequestDTO {

    @NotNull
    private UUID productId;

    @NotNull
    private Map<String, String> variantCombination;

    @Valid
    private SharedMoneyValueDTO price;

    @Valid
    private SharedWeightValueDTO weight;

    @Valid
    private SharedDimensionsValueDTO dimensions;
}