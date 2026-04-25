package com.scotiabank.onboarding.controller;

import com.scotiabank.onboarding.model.request.AccountRequest;
import com.scotiabank.onboarding.model.request.KycRequest;
import com.scotiabank.onboarding.model.request.OnboardingRequest;
import com.scotiabank.onboarding.model.response.*;
import com.scotiabank.onboarding.service.OnboardingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/onboarding")
@RequiredArgsConstructor
@Tag(name = "Customer Onboarding", description = "APIs for managing retail banking customer onboarding lifecycle")
@SecurityRequirement(name = "Bearer Authentication")
public class OnboardingController {

    private final OnboardingService onboardingService;

    // ─── Shared example constants ──────────────────────────────────────────────

    private static final String EXAMPLE_400 = """
            {
              "status": 400,
              "error": "BAD_REQUEST",
              "message": "dateOfBirth is required",
              "timestamp": "2026-04-07T10:00:00Z",
              "path": "/api/v1/onboarding/applications"
            }""";

    private static final String EXAMPLE_401 = """
            {
              "status": 401,
              "error": "UNAUTHORIZED",
              "message": "JWT token is missing or invalid",
              "timestamp": "2026-04-07T10:00:00Z",
              "path": "/api/v1/onboarding/applications"
            }""";

    private static final String EXAMPLE_404 = """
            {
              "status": 404,
              "error": "NOT_FOUND",
              "message": "Onboarding application not found: OB-456",
              "timestamp": "2026-04-07T10:00:00Z",
              "path": "/api/v1/onboarding/applications/OB-456"
            }""";

    private static final String EXAMPLE_409 = """
            {
              "status": 409,
              "error": "CONFLICT",
              "message": "Account creation is not allowed. Current status is KYC_IN_PROGRESS",
              "timestamp": "2026-04-07T10:00:00Z",
              "path": "/api/v1/onboarding/applications/OB-456/accounts"
            }""";

    private static final String EXAMPLE_500 = """
            {
              "status": 500,
              "error": "INTERNAL_SERVER_ERROR",
              "message": "An unexpected error occurred. Please contact support.",
              "timestamp": "2026-04-07T10:00:00Z",
              "path": "/api/v1/onboarding/applications"
            }""";

    // ─── Endpoints ─────────────────────────────────────────────────────────────

    @Operation(
            summary = "Initiate customer onboarding",
            description = """
                    Creates a new onboarding application for a retail banking customer.
                    
                    Returns an applicationId to track progress through subsequent steps.
                    
                    **Next step:** Submit KYC documents using POST /api/v1/onboarding/applications/{applicationId}/kyc
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Onboarding application created successfully",
                    content = @Content(schema = @Schema(implementation = OnboardingResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request — missing or malformed fields",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Bad Request", value = EXAMPLE_400))),
            @ApiResponse(responseCode = "401", description = "Unauthorized — valid JWT required",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Unauthorized", value = EXAMPLE_401))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = EXAMPLE_500)))
    })
    @PostMapping("/applications")
    public ResponseEntity<OnboardingResponse> initiateOnboarding(@Valid @RequestBody OnboardingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(onboardingService.initiateOnboarding(request));
    }

    @Operation(
            summary = "Submit KYC verification documents",
            description = """
                    Submits identity documents for KYC verification for an existing onboarding application.
                    
                    KYC verification is processed asynchronously. The response returns immediately with status KYC_IN_PROGRESS.
                    Poll GET /api/v1/onboarding/applications/{applicationId} to check when KYC is complete.
                    
                    **Prerequisite:** Application must be in INITIATED status.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "KYC documents submitted — verification in progress",
                    content = @Content(schema = @Schema(implementation = KycResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request — missing or malformed fields",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Bad Request", value = EXAMPLE_400))),
            @ApiResponse(responseCode = "401", description = "Unauthorized — valid JWT required",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Unauthorized", value = EXAMPLE_401))),
            @ApiResponse(responseCode = "404", description = "Onboarding application not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = EXAMPLE_404))),
            @ApiResponse(responseCode = "409", description = "Application is not in INITIATED status",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Conflict", value = EXAMPLE_409))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = EXAMPLE_500)))
    })
    @PostMapping("/applications/{applicationId}/kyc")
    public ResponseEntity<KycResponse> submitKyc(
            @Parameter(description = "Onboarding application identifier", example = "OB-456", required = true)
            @PathVariable String applicationId,
            @Valid @RequestBody KycRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(onboardingService.submitKyc(applicationId, request));
    }

    @Operation(
            summary = "Get onboarding application status",
            description = """
                    Returns the current status and next step for an onboarding application.
                    
                    **Status progression:**
                    INITIATED → KYC_IN_PROGRESS → KYC_APPROVED → ACCOUNT_CREATED → COMPLETED
                    
                    If KYC is rejected the status moves to KYC_REJECTED and onboarding cannot proceed.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application status retrieved successfully",
                    content = @Content(schema = @Schema(implementation = StatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized — valid JWT required",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Unauthorized", value = EXAMPLE_401))),
            @ApiResponse(responseCode = "404", description = "Onboarding application not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = EXAMPLE_404))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = EXAMPLE_500)))
    })
    @GetMapping("/applications/{applicationId}")
    public ResponseEntity<StatusResponse> getStatus(
            @Parameter(description = "Onboarding application identifier", example = "OB-456", required = true)
            @PathVariable String applicationId) {
        return ResponseEntity.ok(onboardingService.getStatus(applicationId));
    }

    @Operation(
            summary = "Create bank account",
            description = """
                    Creates a bank account for the customer after KYC approval.
                    
                    **Prerequisite:** Application must be in KYC_APPROVED status.
                    Calling this endpoint in any other status returns 409 Conflict.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Bank account created successfully",
                    content = @Content(schema = @Schema(implementation = AccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request — missing or malformed fields",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Bad Request", value = EXAMPLE_400))),
            @ApiResponse(responseCode = "401", description = "Unauthorized — valid JWT required",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Unauthorized", value = EXAMPLE_401))),
            @ApiResponse(responseCode = "404", description = "Onboarding application not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = EXAMPLE_404))),
            @ApiResponse(responseCode = "409", description = "Application is not in KYC_APPROVED status",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Conflict", value = EXAMPLE_409))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = EXAMPLE_500)))
    })
    @PostMapping("/applications/{applicationId}/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            @Parameter(description = "Onboarding application identifier", example = "OB-456", required = true)
            @PathVariable String applicationId,
            @Valid @RequestBody AccountRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(onboardingService.createAccount(applicationId, request));
    }

    @Operation(
            summary = "Get full onboarding summary",
            description = """
                    Returns a complete summary of the onboarding application including customer profile,
                    KYC verification result, and account details.
                    
                    Useful for completion screens and audit purposes.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Onboarding summary retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SummaryResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized — valid JWT required",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Unauthorized", value = EXAMPLE_401))),
            @ApiResponse(responseCode = "404", description = "Onboarding application not found",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = EXAMPLE_404))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = EXAMPLE_500)))
    })
    @GetMapping("/applications/{applicationId}/summary")
    public ResponseEntity<SummaryResponse> getSummary(
            @Parameter(description = "Onboarding application identifier", example = "OB-456", required = true)
            @PathVariable String applicationId) {
        return ResponseEntity.ok(onboardingService.getSummary(applicationId));
    }
}
