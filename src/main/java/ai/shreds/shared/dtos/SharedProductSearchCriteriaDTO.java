package ai.shreds.shared.dtos;

import lombok.*;
import java.util.UUID;
import ai.shreds.shared.utils.SharedProductStatusEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedProductSearchCriteriaDTO {

    private UUID categoryId;
    private SharedProductStatusEnum status;
    private String search;
}