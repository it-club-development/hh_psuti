package Models.DTOs;

import java.util.UUID;

/**
 * DTO for {@link Models.Company_entity}
 */
public record CompanyRequestDto(String Name, String Site, String Description, String Logo) {
}

