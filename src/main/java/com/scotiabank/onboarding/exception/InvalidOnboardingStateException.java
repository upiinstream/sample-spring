package com.scotiabank.onboarding.exception;

public class InvalidOnboardingStateException extends RuntimeException {
    public InvalidOnboardingStateException(String message) {
        super(message);
    }
}
