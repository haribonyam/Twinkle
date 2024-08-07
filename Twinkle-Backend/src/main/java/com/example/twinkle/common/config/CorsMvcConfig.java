package com.example.twinkle.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//나중에 gateway에서 처리
@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .exposedHeaders("Set-Cookie")
                .allowedOrigins("http://localhost:3000");
    }
}
