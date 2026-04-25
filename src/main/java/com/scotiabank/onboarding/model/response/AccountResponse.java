package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "Response after successful account creation")
public class AccountResponse {

    @Schema(description = "Onboarding application identifier", example = "OB-456")
    private String applicationId;

    @Schema(description = "Newly created bank account number", example = "1234567890")
    private String accountNumber;

    @Schema(description = "Type of account created", example = "CHEQUING")
    private String accountType;

    @Schema(description = "Account currency", example = "CAD")
    private String currency;

    @Schema(description = "Initial deposit amount", example = "100.00")
    private BigDecimal initialDeposit;

    @Schema(description = "Account status", example = "ACTIVE")
    private String status;

    @Schema(description = "Timestamp when account was created", example = "2026-04-07T10:06:00Z")
    private String createdAt;
}
