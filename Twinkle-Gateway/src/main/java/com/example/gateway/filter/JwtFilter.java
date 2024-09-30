package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class JwtFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("GLOBAL FILTER ACCESS!!");

        // JWT 헤더 읽기
        String jwtToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("JWT HEADER : {}", jwtToken);

        // 쿠키 읽기
        StringBuilder cookieBuilder = new StringBuilder();
        Optional.ofNullable(exchange.getRequest().getCookies()).ifPresent(cookies -> {
            cookies.forEach((name, cookie) -> {
                for (var c : cookie) {
                    System.out.println(name);
                    System.out.println(c);
                    cookieBuilder.append(name).append("=").append(c.getValue()).append("; ");
                }
            });
        });

        // 새로운 요청 생성
        var mutatedRequest = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .header(HttpHeaders.COOKIE, cookieBuilder.toString().trim()) // 쿠키 추가
                .build();

        // 수정된 요청으로 체인 진행
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;  // 필터 순서 설정
    }
}
