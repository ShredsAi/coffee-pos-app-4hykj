package ai.shreds.shared.dtos;

import lombok.*;
import java.util.UUID;
import java.time.ZonedDateTime;

import ai.shreds.shared.utils.SharedProductStatusEnum;
import ai.shreds.shared.value_objects.SharedMoneyValueDTO;
import ai.shreds.shared.value_objects.SharedWeightValueDTO;
import ai.shreds.shared.value_objects.SharedDimensionsValueDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedProductResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String shortDescription;
    private String brand;
    private String model;
    private SharedProductStatusEnum status;
    private UUID categoryId;
    private SharedMoneyValueDTO basePrice;
    private SharedWeightValueDTO weight;
    private SharedDimensionsValueDTO dimensions;
    private Long version;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}