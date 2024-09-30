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

    public boolean isExpired(String accessToken) {
        String token = accessToken.split("Bearer ")[1];
        try {
            // 만료 여부를 확인하고 만료된 경우 true를 반환
            return Jwts.parser().verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("token is expired !!: {}", token);
            return true;  // 만료된 경우 true 반환
        } catch (Exception e) {
            log.error("Internal Server Error: {}", e.getMessage());
            return false;
        }
    }


    public String getUserName(String accessToken){
        try {
            String token = accessToken.split("Bearer ")[1];
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
        }catch(ExpiredJwtException e){
            throw ErrorCode.throwTokenIsExpired();
        }
    }


}
