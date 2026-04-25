package com.scotiabank.onboarding.exception;

public class ApplicationNotFoundException extends RuntimeException {
    public ApplicationNotFoundException(String applicationId) {
        super("Onboarding application not found: " + applicationId);
    }
}
