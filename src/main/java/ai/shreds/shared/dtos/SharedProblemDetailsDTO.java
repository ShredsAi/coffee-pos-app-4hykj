package ai.shreds.shared.dtos;

import lombok.*;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedProblemDetailsDTO {

    private String type;
    private String title;
    private Integer status;
    private String detail;
    private String instance;
    private ZonedDateTime timestamp;
}