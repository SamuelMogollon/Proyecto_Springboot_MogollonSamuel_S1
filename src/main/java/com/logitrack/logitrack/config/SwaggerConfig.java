package com.logitrack.logitrack.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "LogiTrack API",
                version = "1.0",
                description = "API REST para el sistema de gestión de bodegas LogiTrack S.A. " +
                        "Permite gestionar usuarios, bodegas, productos, movimientos de inventario y auditoría.",
                contact = @Contact(
                        name = "Samuel Mogollón",
                        email = "samuel@logitrack.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor de desarrollo"),
                @Server(url = "http://172.16.41.19:8080", description = "Servidor en red")
        }

)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Ingresa el token JWT obtenido en /auth/login"
)
public class SwaggerConfig {
}
