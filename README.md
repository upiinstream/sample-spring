# Customer Onboarding API

Retail Banking Customer Onboarding REST API built with **Java 17**, **Spring Boot 3.3.4**, and **SpringDoc OpenAPI**.

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Run the application
```bash
mvn spring-boot:run
```

### Access Swagger UI
Once running, open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### Access raw OpenAPI spec (JSON)
```
http://localhost:8080/api-docs
```

Share the raw OpenAPI spec URL with integration teams — it can be imported directly into Postman, Insomnia, or any API client.

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/onboarding/applications` | Initiate onboarding |
| POST | `/api/v1/onboarding/applications/{applicationId}/kyc` | Submit KYC documents |
| GET | `/api/v1/onboarding/applications/{applicationId}` | Check onboarding status |
| POST | `/api/v1/onboarding/applications/{applicationId}/accounts` | Create bank account |
| GET | `/api/v1/onboarding/applications/{applicationId}/summary` | Get full summary |

---

## Onboarding Flow

```
INITIATED → KYC_IN_PROGRESS → KYC_APPROVED → ACCOUNT_CREATED → COMPLETED
                                    ↓
                               KYC_REJECTED (terminal — onboarding cannot proceed)
```

---

## Authentication

All endpoints require a JWT Bearer token:
```
Authorization: Bearer <your-token>
```

---

## Error Response Format

All errors follow a consistent envelope:
```json
{
  "status": 409,
  "error": "CONFLICT",
  "message": "Account creation is not allowed. Current status is KYC_IN_PROGRESS",
  "timestamp": "2026-04-07T10:00:00Z",
  "path": "/api/v1/onboarding/applications/OB-456/accounts"
}
```

---

## Sample Request — Initiate Onboarding

```bash
curl -X POST http://localhost:8080/api/v1/onboarding/applications \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "dateOfBirth": "1985-03-15",
    "email": "john.smith@email.com",
    "phone": "+1-416-555-0172",
    "address": {
      "street": "123 Main St",
      "city": "Toronto",
      "province": "ON",
      "postalCode": "M5H 2N2",
      "country": "CA"
    },
    "accountType": "CHEQUING"
  }'
```

### Response
```json
{
  "applicationId": "OB-A1B2C3",
  "customerId": "C-D4E5F6",
  "status": "INITIATED",
  "nextStep": "KYC_VERIFICATION",
  "createdAt": "2026-04-07T10:00:00Z"
}
```

---

## Sample Request — Submit KYC

```bash
curl -X POST http://localhost:8080/api/v1/onboarding/applications/OB-A1B2C3/kyc \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "documentType": "PASSPORT",
    "documentNumber": "AB123456",
    "expiryDate": "2030-01-01",
    "issuingCountry": "CA"
  }'
```

### Response
```json
{
  "applicationId": "OB-A1B2C3",
  "kycVerificationId": "KYC-G7H8I9",
  "status": "KYC_IN_PROGRESS",
  "submittedAt": "2026-04-07T10:02:00Z"
}
```

---

## Notes for Integration Teams

- The service uses **stub in-memory storage** — replace `OnboardingService` with actual repository and downstream service calls
- KYC verification is **asynchronous** — poll `GET /applications/{applicationId}` for status updates
- Account creation requires **KYC_APPROVED** status — any other status returns `409 Conflict`
- All PII fields must be **encrypted at rest** in production
- All state changes must be **audit logged** in production

---

## Project Structure

```
src/main/java/com/scotiabank/onboarding/
├── OnboardingApiApplication.java
├── config/
│   └── OpenApiConfig.java
├── controller/
│   └── OnboardingController.java
├── service/
│   └── OnboardingService.java
├── model/
│   ├── request/
│   │   ├── OnboardingRequest.java
│   │   ├── AddressRequest.java
│   │   ├── KycRequest.java
│   │   └── AccountRequest.java
│   └── response/
│       ├── OnboardingResponse.java
│       ├── KycResponse.java
│       ├── AccountResponse.java
│       ├── StatusResponse.java
│       ├── SummaryResponse.java
│       └── ErrorResponse.java
└── exception/
    ├── ApplicationNotFoundException.java
    ├── InvalidOnboardingStateException.java
    └── GlobalExceptionHandler.java
```
