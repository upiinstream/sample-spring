package com.scotiabank.onboarding.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Standard error response envelope")
public class ErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error code", example = "BAD_REQUEST")
    private String error;

    @Schema(description = "Human-readable error message", example = "dateOfBirth is required")
    private String message;

    @Schema(description = "Timestamp of the error", example = "2026-04-07T10:00:00Z")
    private String timestamp;

    @Schema(description = "Request path that caused the error", example = "/api/v1/onboarding/applications")
    private String path;
}
