package ai.shreds.application.services;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationCoordinationResultDTO {
    private boolean success;
    private UUID productId;
    private Long version;
    private ZonedDateTime timestamp;
}