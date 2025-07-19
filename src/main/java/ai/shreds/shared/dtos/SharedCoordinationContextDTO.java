package ai.shreds.shared.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedCoordinationContextDTO {

    private String context;
    private String requestId;
}