package com.restaurant.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Restaurantes")
                        .description("API de gestão de restaurantes - FIAP Postech Tech Challenge Fase 2. "
                                + "Cobre tipos de usuário, usuários, restaurantes e itens de cardápio.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("https://github.com/rodrfit/restaurant-management"))
                        .contact(new Contact()
                                .name("FIAP Postech Arquitetura e Desenvolvimento em Java")
                                .email("contato@restaurante.com")));
    }
}