package Models.DTOs.Response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link Models.Response_entity}
 */
public record ResponseResponseDto(UUID Student_ID, UUID Vacancy_ID, boolean Status, String Cover_letter,
                                  LocalDateTime Created_at, LocalDateTime Updated_at) {
}