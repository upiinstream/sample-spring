package com.scotiabank.onboarding.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request payload to initiate a customer onboarding application")
public class OnboardingRequest {

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Schema(description = "Customer's legal first name", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Schema(description = "Customer's legal last name", example = "Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be in YYYY-MM-DD format")
    @Schema(description = "Customer's date of birth in YYYY-MM-DD format", example = "1985-03-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dateOfBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Customer's email address", example = "john.smith@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+1-\\d{3}-\\d{3}-\\d{4}$", message = "Phone must be in +1-XXX-XXX-XXXX format")
    @Schema(description = "Customer's phone number", example = "+1-416-555-0172", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Valid
    @NotNull(message = "Address is required")
    @Schema(description = "Customer's residential address", requiredMode = Schema.RequiredMode.REQUIRED)
    private AddressRequest address;

    @NotNull(message = "Account type is required")
    @Schema(description = "Type of bank account to open", example = "CHEQUING", allowableValues = {"CHEQUING", "SAVINGS"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountType;
}
