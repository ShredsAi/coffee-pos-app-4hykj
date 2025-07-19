package ai.shreds.shared.dtos;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCategoryTreeResponseDTO {

    private UUID id;
    private String name;
    private String description;
    private String path;
    private Integer level;
    private Integer displayOrder;
    private Boolean isActive;
    private List<SharedCategoryTreeResponseDTO> children;
}