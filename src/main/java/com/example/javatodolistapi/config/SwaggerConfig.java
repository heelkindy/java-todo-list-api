package com.example.javatodolistapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Todo List API")
                        .description("API documentation for the TodoList application built with Spring Boot and Swagger")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hieu Le")
                                .email("hieu@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description(
                                "In this project you are required to develop a RESTful API to allow users to manage their to-do list. The previous backend projects have only focused on the CRUD operations, but this project will require you to implement user authentication as well.")
                        .url("https://github.com/heelkindy/java-todo-list-api"));
    }
}
