package ai.shreds.shared.dtos;

import lombok.*;
import java.util.UUID;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCategoryResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private UUID parentCategoryId;
    private String path;
    private Integer level;
    private Integer displayOrder;
    private Boolean isActive;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}