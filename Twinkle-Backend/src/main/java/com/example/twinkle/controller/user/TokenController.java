package com.example.twinkle.controller.user;


import com.example.twinkle.dto.response.TokenResponseDto;
import com.example.twinkle.service.user.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

        private final RefreshTokenService refreshTokenService;
    @PostMapping("/user/logout")
    public void logout(@RequestHeader("Authorization") final String access){
        refreshTokenService.deleteRefreshToken(access);
    }
    @PostMapping("/user/token/refresh")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader("Authorization") final String access){
        String accessToken = refreshTokenService.republishAccessToken(access);
        if(accessToken != null){

            return ResponseEntity.ok(new TokenResponseDto(accessToken));
        }
        return ResponseEntity.badRequest().body(new TokenResponseDto(null));
    }
}
