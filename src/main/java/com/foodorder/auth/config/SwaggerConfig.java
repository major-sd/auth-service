package com.foodorder.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI authServiceOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("Auth Service - Local Development");

        Server gatewayServer = new Server();
        gatewayServer.setUrl("http://localhost:8080/auth");
        gatewayServer.setDescription("Auth Service - Via API Gateway");

        Contact contact = new Contact();
        contact.setEmail("support@foodordering.com");
        contact.setName("Food Ordering Team");
        contact.setUrl("https://www.foodordering.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Auth Service API - Food Ordering System")
                .version("1.0.0")
                .contact(contact)
                .description("Authentication and Authorization Service API for user registration, login, and JWT token management.")
                .termsOfService("https://www.foodordering.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer, gatewayServer));
    }
}
