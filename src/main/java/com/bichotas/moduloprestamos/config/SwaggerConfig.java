package com.bichotas.moduloprestamos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Configures the OpenAPI Swagger documentation for the Modulo Prestamos API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the OpenAPI documentation for the Modulo Prestamos API.
     * <p>
     * This method sets up the OpenAPI components, including security schemes and
     * security requirements, and provides metadata information such as the title,
     * version, description, and contact details for the API.
     *
     * @return an instance of {@link OpenAPI} with the configured settings.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Modulo Prestamos API")
                        .version("1.0")
                        .description("API para el módulo de préstamos de la biblioteca BiblioSoft")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Bichotas TEAM")
                                .url("https://github.com/Bichotas-Season/BichotaB")
                        )
                );
    }
}
