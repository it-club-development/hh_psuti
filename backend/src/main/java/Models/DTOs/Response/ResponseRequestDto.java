package Models.DTOs.Response;

import java.util.UUID;

/**
 * DTO for {@link Models.Response_entity}
 */
public record ResponseRequestDto(UUID Student_ID, UUID Vacancy_ID, boolean Status, String Cover_letter) {
}