package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Response after submitting KYC documents")
public class KycResponse {

    @Schema(description = "Onboarding application identifier", example = "OB-456")
    private String applicationId;

    @Schema(description = "Unique KYC verification identifier", example = "KYC-789")
    private String kycVerificationId;

    @Schema(description = "Current KYC status", example = "KYC_IN_PROGRESS")
    private String status;

    @Schema(description = "Timestamp when KYC was submitted", example = "2026-04-07T10:02:00Z")
    private String submittedAt;
}
