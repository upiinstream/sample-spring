package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response after initiating an onboarding application")
public class OnboardingResponse {

    @Schema(description = "Unique onboarding application identifier", example = "OB-456")
    private String applicationId;

    @Schema(description = "Unique customer identifier", example = "C123456")
    private String customerId;

    @Schema(description = "Current onboarding status",
            allowableValues = {"INITIATED", "KYC_IN_PROGRESS", "KYC_APPROVED", "KYC_REJECTED", "ACCOUNT_CREATED", "COMPLETED", "FAILED"},
            example = "INITIATED")
    private String status;

    @Schema(description = "Next step in the onboarding flow", example = "KYC_VERIFICATION")
    private String nextStep;

    @Schema(description = "Timestamp when application was created", example = "2026-04-07T10:00:00Z")
    private String createdAt;
}
