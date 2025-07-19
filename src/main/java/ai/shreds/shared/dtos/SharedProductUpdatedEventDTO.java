package ai.shreds.shared.dtos;

import lombok.*;
import java.util.UUID;
import java.util.Map;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedProductUpdatedEventDTO {

    private UUID eventId;
    private String eventType;
    private UUID productId;
    private String changeType;
    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;
    private ZonedDateTime timestamp;
}