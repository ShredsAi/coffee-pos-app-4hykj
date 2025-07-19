package ai.shreds.shared.value_objects;

import lombok.*;
import java.util.UUID;
import ai.shreds.shared.utils.SharedProductStatusEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedProductSearchParams {

    private Integer page;
    private Integer size;
    private UUID categoryId;
    private SharedProductStatusEnum status;
    private String search;
}