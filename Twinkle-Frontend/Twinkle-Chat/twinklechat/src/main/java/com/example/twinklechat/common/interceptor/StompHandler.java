package com.example.twinklechat.common.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final JwtHandler jwtHandler;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // StompCommand에 따라 분기해서 로직 구현.

        if(StompCommand.CONNECT == accessor.getCommand()) {
            log.info("connect pro");

            String accessToken = getAccessToken(accessor);
            if (accessToken == null) {
                log.info("토큰 없어유");
                throw new IllegalArgumentException("Authorization token is null");
                //Exception 처리해야함
            }
            /*
            if (isExpired(accessToken)) {
                //Exception 처리해야함
                throw new IllegalArgumentException("Authorization token is expired");
            }
            */
            String username = getUsername(accessToken);
            System.out.println(username);
            log.info("StompAccessor = {}", username);
        }
        return message;
    }



    private String getUsername(String accessToken) {
        log.info("getUsername ing");
       String username= jwtHandler.getUserName(accessToken);
       log.info("username :: {}",username);
       return username;
    }


    private boolean isExpired(String accessToken) {
        log.info("isExpired ing");
        return jwtHandler.isExpired(accessToken);
    }

    public String getAccessToken(StompHeaderAccessor accessor){
        String token = accessor.getFirstNativeHeader("Authorization");
        log.info("token = {}",token);
        return token;
    }
}
