package com.boubyan.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")  // allow all origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // allow all http methods
                .allowedHeaders("*");  // allow all headers
    }
}
