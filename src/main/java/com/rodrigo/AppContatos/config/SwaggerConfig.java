package com.rodrigo.AppContatos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("basicScheme", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                        )
                )
                .info(new Info()
                        .title("API de Controle de Contatos")
                        .description("API para gerenciar pessoas e seus contatos.")
                        .contact(new Contact()
                                .name("Rodrigo")
                                .email("digoh.a4@Gmail.com.com")
                                .url("https://www.linkedin.com/in/rodrigo-dias-196985236/")
                        )
                        .version("0.0.1")
                );

    }

    @Bean
    OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {

        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations()
                    .forEach(operation -> {
                        ApiResponses apiResponses = operation.getResponses();

                        apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
                        apiResponses.addApiResponse("201", createApiResponse("Objeto Persistido!"));
                        apiResponses.addApiResponse("204", createApiResponse("Objeto Excluído!"));
                        apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
                        apiResponses.addApiResponse("404", createApiResponse("Objeto não encontrado!"));
                        apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));

                    }));
        };
    }
    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }

}