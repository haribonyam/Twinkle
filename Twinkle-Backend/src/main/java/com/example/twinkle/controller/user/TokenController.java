package com.example.twinkle.controller.user;


import com.example.twinkle.dto.response.TokenResponseDto;
import com.example.twinkle.service.user.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

        private final RefreshTokenService refreshTokenService;
    @DeleteMapping("/token")
    public void logout(@RequestHeader("Authorization") final String access){
        refreshTokenService.deleteRefreshToken(access);
    }
    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestHeader("Authorization") final String access){
        System.out.println(access.split("Bearer ")[1]);
        String accessToken = refreshTokenService.republishAccessToken(access.split("Bearer ")[1]);
        if(accessToken != null){

            return ResponseEntity.ok(new TokenResponseDto("Bearer "+accessToken));
        }
        return ResponseEntity.badRequest().body(new TokenResponseDto(null));
    }
}
