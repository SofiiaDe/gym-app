package com.xstack.gymapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI usersMicroserviceOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Gym App")
            .description(
                "The application enables gym trainees and trainers to register their profile, manage their trainings, giving trainees the option to select one or more trainers, etc.")
            .version("1.0"));
  }

}
