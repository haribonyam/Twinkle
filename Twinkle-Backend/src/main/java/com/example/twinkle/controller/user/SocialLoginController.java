package com.example.twinkle.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SocialLoginController {

    @PostMapping("/social")
    public ResponseEntity<?> sendToken(HttpServletRequest request) {
        log.info("Social Login send token");
        Cookie[] cookies = request.getCookies();
        String id = "";
        String token = "Bearer ";
        String nickname = "";
        for (Cookie cookie : cookies) {
            if (cookie.getValue() == null) {
                return ResponseEntity.badRequest().body("No Cookie found");
            }
            switch (cookie.getName()) {
                case "Authorization":
                    token += cookie.getValue();
                    break;
                case "Id":
                    id = cookie.getValue();
                    break;
                case "Nickname":
                    nickname = cookie.getValue();
                    break;
            }
        }

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("jwtToken", token);
        responseBody.put("nickname", nickname);
        responseBody.put("id", id);
        return ResponseEntity.ok(responseBody);
    }
}
