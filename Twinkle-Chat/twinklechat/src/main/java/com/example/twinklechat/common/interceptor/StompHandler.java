package com.example.twinklechat.common.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
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
        log.info("Opening STOMP Socket");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // StompCommand에 따라 분기해서 로직 구현.

      //  if(StompCommand.CONNECT == accessor.getCommand()) {
            String accessToken = getAccessToken(accessor);
            if (accessToken == null) {

                throw new MessageDeliveryException("Authorization token is null");
            }

            if (isExpired(accessToken)) {
                throw new MessageDeliveryException("Authorization token is expired");
            }

            String username = getUsername(accessToken);

        //}
/*
        if(StompCommand.SEND == accessor.getCommand()){
            log.info("message send pro");
            String accessToken = getAccessToken(accessor);
            if (accessToken == null) {

                throw new MessageDeliveryException("Authorization token is null");
            }

            if (!isExpired(accessToken)) {
                throw new MessageDeliveryException("Authorization token is expired");
            }
       }*/
        return message;
    }



    private String getUsername(String accessToken) {
       String username= jwtHandler.getUserName(accessToken);
       return username;
    }


    private boolean isExpired(String accessToken) {
        return jwtHandler.isExpired(accessToken);
    }

    public String getAccessToken(StompHeaderAccessor accessor){
        String token = accessor.getFirstNativeHeader("Authorization");
        return token;
    }
}
