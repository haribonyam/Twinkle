package com.example.twinkle.service.user;

import com.example.twinkle.common.config.auth.JwtUtil;
import com.example.twinkle.domain.redis.RefreshToken;
import com.example.twinkle.repository.user.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public void saveToken(String access, String refresh,String username) {
                refreshTokenRepository.save(RefreshToken.builder()
                        .refreshToken(refresh)
                        .accessToken(access)
                        .username(username)
                        .build());
    }
    @Transactional
    public void deleteRefreshToken(String access){
        RefreshToken token = refreshTokenRepository.findByAccessToken(access).orElseThrow(IllegalArgumentException::new);
        refreshTokenRepository.delete(token);
    }

    @Transactional
    public String republishAccessToken(String access){

        log.info("jwt token refresh ing");
        Optional<RefreshToken> refresh = refreshTokenRepository.findByAccessToken(access);

        if(refresh.isPresent() && !jwtUtil.isExpired(refresh.get().getRefreshToken())){

            RefreshToken refreshToken = refresh.get();

            String role = refreshToken.getUsername().equals("ADMIN")?"ROLE_ADMIN":"ROLE_USER";

            String newAccessToken = jwtUtil.createJwt(refreshToken.getUsername(),role);

            refreshToken.updateAccessToken(newAccessToken);

            log.info("access token is expired & new token is published");
            return newAccessToken;
        }
        log.info("jwt refresh fail...");
        return null;
    }


}
