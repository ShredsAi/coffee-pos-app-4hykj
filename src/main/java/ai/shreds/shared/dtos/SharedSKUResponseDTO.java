package ai.shreds.shared.dtos;

import lombok.*;
import java.util.UUID;
import java.util.Map;
import java.time.ZonedDateTime;

import ai.shreds.shared.value_objects.SharedMoneyValueDTO;
import ai.shreds.shared.value_objects.SharedWeightValueDTO;
import ai.shreds.shared.value_objects.SharedDimensionsValueDTO;
import ai.shreds.shared.utils.SharedProductStatusEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedSKUResponseDTO {

    private UUID id;
    private String code;
    private UUID productId;
    private Map<String, String> variantCombination;
    private SharedMoneyValueDTO price;
    private SharedWeightValueDTO weight;
    private SharedDimensionsValueDTO dimensions;
    private SharedProductStatusEnum status;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}