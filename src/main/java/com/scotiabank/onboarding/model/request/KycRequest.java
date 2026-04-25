package com.scotiabank.onboarding.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "KYC identity verification document submission")
public class KycRequest {

    @NotBlank(message = "Document type is required")
    @Schema(description = "Type of identity document", example = "PASSPORT",
            allowableValues = {"PASSPORT", "DRIVERS_LICENSE", "NATIONAL_ID"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String documentType;

    @NotBlank(message = "Document number is required")
    @Schema(description = "Identity document number", example = "AB123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String documentNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Expiry date must be in YYYY-MM-DD format")
    @Schema(description = "Document expiry date in YYYY-MM-DD format", example = "2030-01-01", requiredMode = Schema.RequiredMode.REQUIRED)
    private String expiryDate;

    @NotBlank(message = "Issuing country is required")
    @Schema(description = "ISO country code of issuing authority", example = "CA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String issuingCountry;
}
