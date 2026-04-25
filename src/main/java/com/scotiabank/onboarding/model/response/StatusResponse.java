package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Current status of an onboarding application")
public class StatusResponse {

    @Schema(description = "Onboarding application identifier", example = "OB-456")
    private String applicationId;

    @Schema(description = "Customer identifier", example = "C123456")
    private String customerId;

    @Schema(description = "Current onboarding status",
            allowableValues = {"INITIATED", "KYC_IN_PROGRESS", "KYC_APPROVED", "KYC_REJECTED", "ACCOUNT_CREATED", "COMPLETED", "FAILED"},
            example = "KYC_APPROVED")
    private String status;

    @Schema(description = "Next step in the onboarding flow", example = "ACCOUNT_CREATION")
    private String nextStep;

    @Schema(description = "KYC risk rating if available", allowableValues = {"LOW", "MEDIUM", "HIGH"}, example = "LOW")
    private String kycRiskRating;

    @Schema(description = "Account type requested", example = "CHEQUING")
    private String accountType;

    @Schema(description = "Timestamp when application was created", example = "2026-04-07T10:00:00Z")
    private String createdAt;

    @Schema(description = "Timestamp of last status update", example = "2026-04-07T10:05:00Z")
    private String lastUpdatedAt;
}
