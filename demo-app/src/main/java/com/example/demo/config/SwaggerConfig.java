package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Example - Rest API")
                        .description("Example API with SpringBoot and H2")
                        .version("1.0")
                        .termsOfService("Terms of use: Open Source")
                        )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Osvaldo Melo")
                                .url("http://www.redhat.com"));
    }
}
