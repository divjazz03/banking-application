package com.example.bankingapplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Divjazz Bank App",
                description = "Backend Rest APIs for DF Bank",
                version = "v1.0",
                contact = @Contact(
                        name = "Maduka Divine",
                        email = "divjazz03@gmail.com",
                        url = "https://github.com/divjazz03/DF_bank_app"
                ),
                license = @License(
                        name = "Maduka Divine",
                        url = "https://github.com/divjazz03/DF_bank_app"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Divjazz Bank App Documentation",
                url = "https://github.com/divjazz03/DF_bank_app"
        )
)
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

}
