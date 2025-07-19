package ai.shreds.shared.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

import lombok.*;

import java.util.UUID;

import ai.shreds.shared.value_objects.SharedMoneyValueDTO;
import ai.shreds.shared.value_objects.SharedWeightValueDTO;
import ai.shreds.shared.value_objects.SharedDimensionsValueDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCreateProductRequestDTO {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Size(max = 500)
    private String shortDescription;

    @Size(max = 100)
    private String brand;

    @Size(max = 100)
    private String model;

    @NotNull
    private UUID categoryId;

    @NotNull
    @Valid
    private SharedMoneyValueDTO basePrice;

    @Valid
    private SharedWeightValueDTO weight;

    @Valid
    private SharedDimensionsValueDTO dimensions;
}