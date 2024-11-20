package com.bichotas.moduloprestamos.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;

@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "Modulo Prestamos API",
                version = "1.0",
                description = "API para el módulo de préstamos de la biblioteca",
                contact = @Contact(
                        name = "Bichotas TEAM"
                        )
        )
)
public class SwaggerConfig {

}
