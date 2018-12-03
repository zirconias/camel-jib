package com.example.demo;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class SwaggerRedirectController {
    // Redirect to access swagger UI via short URL from "/swagger-ui" to "/swagger-ui/index.html?url=/api/swagger&validatorUrl="
    @Controller
    class SwaggerWelcome {
        @GetMapping("/swagger-ui")
        public String redirectToUi() {
            return "redirect:/webjars/swagger-ui/index.html?url=/api/api-doc&validatorUrl=";
        }
    }
}