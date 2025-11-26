package com.example.restaurant.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant Rating API",
                version = "1.0",
                description = "API системы оценки ресторанов"
        )
)
public class OpenApiConfig {
}
