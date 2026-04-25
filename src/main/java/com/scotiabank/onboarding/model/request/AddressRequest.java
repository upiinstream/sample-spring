package com.scotiabank.onboarding.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Customer residential address")
public class AddressRequest {

    @NotBlank(message = "Street is required")
    @Schema(description = "Street address", example = "123 Main St", requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @NotBlank(message = "City is required")
    @Schema(description = "City", example = "Toronto", requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @NotBlank(message = "Province is required")
    @Schema(description = "Province code", example = "ON", requiredMode = Schema.RequiredMode.REQUIRED)
    private String province;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "[A-Z]\\d[A-Z] \\d[A-Z]\\d", message = "Postal code must be in A1A 1A1 format")
    @Schema(description = "Canadian postal code", example = "M5H 2N2", requiredMode = Schema.RequiredMode.REQUIRED)
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Schema(description = "ISO country code", example = "CA", requiredMode = Schema.RequiredMode.REQUIRED)
    private String country;
}
