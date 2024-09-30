package com.example.twinklechat.common.interceptor;

import com.example.twinklechat.common.exception.CustomException;
import com.example.twinklechat.common.exception.ErrorCode;
import com.example.twinklechat.common.exception.handler.ExceptionResponseHandler;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class JwtHandler {

    private SecretKey secretKey;


    public JwtHandler(@Value("${jwt-secret-key}") String key){
        this.secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isExpired(String accessToken){
        String token = accessToken.split("Bearer ")[1];
        log.info("jwt handler expired pro");
       try {
          return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
       }catch(ExpiredJwtException e){
           log.info("토큰 만료 {}",token);
           ErrorCode.throwTokenIsExpired();
           return false;
       }
    }

    public String getUserName(String accessToken){
        log.info("jwt handler get user name pro");
        try {
            String token = accessToken.split("Bearer ")[1];
            System.out.println(token);
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
        }catch(ExpiredJwtException e){
            log.info("{}",e);
            ErrorCode.throwUnauthorizedAccess();
            return null;
        }
    }


}
