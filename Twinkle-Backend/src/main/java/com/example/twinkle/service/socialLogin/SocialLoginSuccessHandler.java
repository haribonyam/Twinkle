package com.example.twinkle.service.socialLogin;

import com.example.twinkle.common.config.auth.JwtUtil;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.redis.RefreshToken;
import com.example.twinkle.repository.MemberRepository;
import com.example.twinkle.repository.user.RefreshTokenRepository;
import com.example.twinkle.service.user.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class SocialLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String username = customUserDetails.getUsername();
        MemberEntity member = memberRepository.findByUsername(username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(username, role);
        String refresh = jwtUtil.createRefreshToken(username);

        refreshTokenService.saveToken(access,refresh,username);

        response.addCookie(createCookie("Authorization", access));
        response.addCookie(createCookie("Id", member.getId().toString()));
        response.addCookie(createCookie("Nickname", member.getNickname()));
        response.sendRedirect("http://localhost:3000/social");

    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/social");
        cookie.setHttpOnly(false);

        return cookie;
    }
}
