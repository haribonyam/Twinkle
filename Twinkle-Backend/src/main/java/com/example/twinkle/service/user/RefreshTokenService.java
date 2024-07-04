package com.example.twinkle.service.user;

import com.example.twinkle.common.config.auth.JwtUtil;
import com.example.twinkle.domain.redis.RefreshToken;
import com.example.twinkle.repository.user.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public void saveToken(String access, String refresh,String username) {
                refreshTokenRepository.save(RefreshToken.builder()
                        .refreshToken(refresh)
                        .accessToken(access)
                        .username(username)
                        .build());
    }

    public void deleteRefreshToken(String access){
        RefreshToken token = refreshTokenRepository.findByAccessToken(access).orElseThrow(IllegalArgumentException::new);
        refreshTokenRepository.delete(token);
    }

    public String republishAccessToken(String access){
        Optional<RefreshToken> refresh = refreshTokenRepository.findByAccessToken(access);
        if(refresh.isPresent() && jwtUtil.isExpired(refresh.get().getRefreshToken())){
            RefreshToken refreshToken = refresh.get();
            String newAccessToken = jwtUtil.createJwt(refreshToken.getUsername());
            refreshToken.updateAccessToken(newAccessToken);
            refreshTokenRepository.save(refreshToken);
            return newAccessToken;
        }
        return null;
    }


}
