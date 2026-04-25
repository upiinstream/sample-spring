package com.scotiabank.onboarding.service;

import com.scotiabank.onboarding.exception.ApplicationNotFoundException;
import com.scotiabank.onboarding.exception.InvalidOnboardingStateException;
import com.scotiabank.onboarding.model.request.AccountRequest;
import com.scotiabank.onboarding.model.request.KycRequest;
import com.scotiabank.onboarding.model.request.OnboardingRequest;
import com.scotiabank.onboarding.model.response.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stub implementation for demonstration and API contract purposes.
 * Replace with actual business logic, repository calls, and downstream service integration.
 */
@Service
public class OnboardingService {

    // In-memory store for demo — replace with JPA repository
    private final Map<String, String> applicationStatusStore = new ConcurrentHashMap<>();

    public OnboardingResponse initiateOnboarding(OnboardingRequest request) {
        String applicationId = "OB-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String customerId = "C-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        applicationStatusStore.put(applicationId, "INITIATED");

        return OnboardingResponse.builder()
                .applicationId(applicationId)
                .customerId(customerId)
                .status("INITIATED")
                .nextStep("KYC_VERIFICATION")
                .createdAt(Instant.now().toString())
                .build();
    }

    public KycResponse submitKyc(String applicationId, KycRequest request) {
        String currentStatus = applicationStatusStore.get(applicationId);
        if (currentStatus == null) throw new ApplicationNotFoundException(applicationId);
        if (!"INITIATED".equals(currentStatus)) {
            throw new InvalidOnboardingStateException(
                    "KYC submission is not allowed. Current status is " + currentStatus);
        }
        applicationStatusStore.put(applicationId, "KYC_IN_PROGRESS");

        return KycResponse.builder()
                .applicationId(applicationId)
                .kycVerificationId("KYC-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase())
                .status("KYC_IN_PROGRESS")
                .submittedAt(Instant.now().toString())
                .build();
    }

    public StatusResponse getStatus(String applicationId) {
        String currentStatus = applicationStatusStore.get(applicationId);
        if (currentStatus == null) throw new ApplicationNotFoundException(applicationId);

        String nextStep = switch (currentStatus) {
            case "INITIATED" -> "KYC_VERIFICATION";
            case "KYC_IN_PROGRESS" -> "AWAITING_KYC_RESULT";
            case "KYC_APPROVED" -> "ACCOUNT_CREATION";
            case "ACCOUNT_CREATED" -> "COMPLETED";
            default -> "N/A";
        };

        return StatusResponse.builder()
                .applicationId(applicationId)
                .customerId("C123456")
                .status(currentStatus)
                .nextStep(nextStep)
                .kycRiskRating("KYC_APPROVED".equals(currentStatus) ? "LOW" : null)
                .accountType("CHEQUING")
                .createdAt(Instant.now().minusSeconds(300).toString())
                .lastUpdatedAt(Instant.now().toString())
                .build();
    }

    public AccountResponse createAccount(String applicationId, AccountRequest request) {
        String currentStatus = applicationStatusStore.get(applicationId);
        if (currentStatus == null) throw new ApplicationNotFoundException(applicationId);
        if (!"KYC_APPROVED".equals(currentStatus)) {
            throw new InvalidOnboardingStateException(
                    "Account creation is not allowed. Current status is " + currentStatus);
        }
        applicationStatusStore.put(applicationId, "ACCOUNT_CREATED");

        return AccountResponse.builder()
                .applicationId(applicationId)
                .accountNumber(String.valueOf(System.currentTimeMillis()).substring(3))
                .accountType(request.getAccountType())
                .currency(request.getCurrency())
                .initialDeposit(request.getInitialDeposit())
                .status("ACTIVE")
                .createdAt(Instant.now().toString())
                .build();
    }

    public SummaryResponse getSummary(String applicationId) {
        String currentStatus = applicationStatusStore.get(applicationId);
        if (currentStatus == null) throw new ApplicationNotFoundException(applicationId);

        return SummaryResponse.builder()
                .applicationId(applicationId)
                .customer(SummaryResponse.CustomerSummary.builder()
                        .customerId("C123456")
                        .firstName("John")
                        .lastName("Smith")
                        .email("john.smith@email.com")
                        .build())
                .kyc(SummaryResponse.KycSummary.builder()
                        .verificationId("KYC-789")
                        .status("APPROVED")
                        .riskRating("LOW")
                        .verifiedAt(Instant.now().minusSeconds(60).toString())
                        .build())
                .account(SummaryResponse.AccountSummary.builder()
                        .accountNumber("1234567890")
                        .accountType("CHEQUING")
                        .status("ACTIVE")
                        .build())
                .onboardingStatus(currentStatus)
                .completedAt(Instant.now().toString())
                .build();
    }
}
