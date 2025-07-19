package ai.shreds.shared.dtos;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedPagedProductResponseDTO {

    private List<SharedProductResponseDTO> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean first;
    private Boolean last;
}