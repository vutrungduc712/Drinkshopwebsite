package com.trungduc.drinkshop;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition()
public class DrinkshopwebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrinkshopwebsiteApplication.class, args);
    }


}
