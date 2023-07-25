package com.moneymedia.demo.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    private val BEARER_SECURITY_SCHEME = "Bearer"

    // Leave an eye-catcher of "not_set" in case the version gets lost
    @Bean
    fun openApi(): OpenAPI =
        OpenAPI()
            .info(Info()
                .title("Swagger API key authorization demo")
                .contact(Contact()
                    .name("Greg Wiley").email("gwiley@money-media.com")))
            .security(listOf(SecurityRequirement()
                .addList(BEARER_SECURITY_SCHEME)))
            .components(Components()
                .addSecuritySchemes(BEARER_SECURITY_SCHEME, SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.APIKEY)
                    .`in`(SecurityScheme.In.HEADER)))

    @Bean
    fun groupedOpenApi(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("demo").pathsToMatch("/api/**")
            .build()

}
