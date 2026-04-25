package com.scotiabank.onboarding.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Customer Onboarding API")
                        .version("1.0.0")
                        .description("""
                                Retail Banking Customer Onboarding API.
                                
                                This API manages the end-to-end onboarding flow for retail banking customers:
                                - Initiate an onboarding application
                                - Submit KYC verification documents
                                - Track onboarding status
                                - Complete account creation
                                - Retrieve full onboarding summary
                                
                                **Onboarding Flow:**
                                INITIATED → KYC_IN_PROGRESS → KYC_APPROVED → ACCOUNT_CREATED → COMPLETED
                                """)
                        .contact(new Contact()
                                .name("Wealth Platform Team")
                                .email("wealth-platform@scotiabank.com"))
                        .license(new License()
                                .name("Internal Use Only")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development"),
                        new Server().url("https://api-dev.scotiabank.com").description("Development"),
                        new Server().url("https://api-sit.scotiabank.com").description("SIT"),
                        new Server().url("https://api-uat.scotiabank.com").description("UAT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT token required. Include in Authorization header as: Bearer {token}")));
    }
}
