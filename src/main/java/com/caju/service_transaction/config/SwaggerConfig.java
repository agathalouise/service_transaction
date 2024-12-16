package com.caju.service_transaction.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Autorização de Transações",
                version = "1.0",
                description = "API para processar transações com validação de segurança."
        )
)
public class SwaggerConfig {
    // Configurações personalizadas podem ser adicionadas aqui
}