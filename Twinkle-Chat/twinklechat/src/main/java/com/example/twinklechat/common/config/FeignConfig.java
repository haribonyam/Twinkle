package com.example.twinklechat.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                //나중에 헤더에서 가져오기
                String jwtToken = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJ1c2VybmFtZSI6InRlc3QxMjMiLCJyb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzIxODE4ODk0LCJleHAiOjE4MTE4MTg4OTR9.8YfpfOB7Bkp8LbfESvrucL55uZl3jt7z5IGXC728a9KNu8OYRRCBl4R-ArEn_wWf";

                requestTemplate.header("Authorization",jwtToken);
            }
        };
    }
}
