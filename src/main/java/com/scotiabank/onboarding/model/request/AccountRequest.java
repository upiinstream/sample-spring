package com.scotiabank.onboarding.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Request to create a bank account after KYC approval")
public class AccountRequest {

    @NotBlank(message = "Account type is required")
    @Schema(description = "Type of bank account", example = "CHEQUING",
            allowableValues = {"CHEQUING", "SAVINGS"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountType;

    @NotBlank(message = "Currency is required")
    @Schema(description = "ISO currency code", example = "CAD",
            allowableValues = {"CAD", "USD"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String currency;

    @NotNull(message = "Initial deposit amount is required")
    @DecimalMin(value = "0.01", message = "Initial deposit must be greater than 0")
    @Schema(description = "Initial deposit amount", example = "100.00", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal initialDeposit;
}
