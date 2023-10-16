package org.makaia.transactionBankingSystem.swagger;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration{
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Sistema de transacciones bancarias.")
                        .description("API REST para gestión de un sistema " +
                                "de  transacciones bancarias. Su es simular " +
                                "un sistema de transacciones bancarias básico" +
                                " donde los usuarios podrán abrir cuentas, " +
                                "realizar depósitos, transferir dinero entre " +
                                "cuentas, además de crear y transferir a " +
                                "bolsillos desde sus cuentas.\n\n Elaborada " +
                                "por:\n- Paula Múnera.\n- Yeisson " +
                                "Vahos.\n\n Proyecto integrador bootcamp " +
                                "MAKAIA.")
                        .version("1.0.0")
                );
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
