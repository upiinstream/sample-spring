package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Full onboarding summary including customer, KYC, and account details")
public class SummaryResponse {

    @Schema(description = "Onboarding application identifier", example = "OB-456")
    private String applicationId;

    @Schema(description = "Customer details")
    private CustomerSummary customer;

    @Schema(description = "KYC verification details")
    private KycSummary kyc;

    @Schema(description = "Bank account details")
    private AccountSummary account;

    @Schema(description = "Overall onboarding status", example = "COMPLETED")
    private String onboardingStatus;

    @Schema(description = "Timestamp when onboarding was completed", example = "2026-04-07T10:06:00Z")
    private String completedAt;

    @Data
    @Builder
    @Schema(description = "Customer summary details")
    public static class CustomerSummary {
        @Schema(example = "C123456") private String customerId;
        @Schema(example = "John") private String firstName;
        @Schema(example = "Smith") private String lastName;
        @Schema(example = "john.smith@email.com") private String email;
    }

    @Data
    @Builder
    @Schema(description = "KYC verification summary")
    public static class KycSummary {
        @Schema(example = "KYC-789") private String verificationId;
        @Schema(example = "APPROVED") private String status;
        @Schema(example = "LOW") private String riskRating;
        @Schema(example = "2026-04-07T10:05:00Z") private String verifiedAt;
    }

    @Data
    @Builder
    @Schema(description = "Bank account summary")
    public static class AccountSummary {
        @Schema(example = "1234567890") private String accountNumber;
        @Schema(example = "CHEQUING") private String accountType;
        @Schema(example = "ACTIVE") private String status;
    }
}
