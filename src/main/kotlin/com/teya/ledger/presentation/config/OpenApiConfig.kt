package com.teya.ledger.presentation.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Teya Ledger API")
                    .description("A simple ledger API to record money movements, view balance, and transaction history")
                    .version("1.0.0")
            )
    }
}
