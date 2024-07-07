package com.example.twinkle.common.config.auth;


import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.dto.CustomUserDetails;
import com.example.twinkle.service.user.RefreshTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

//나중에 gateway global 이동
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    public JwtFilter(JwtUtil jwtUtil,RefreshTokenService refreshTokenService){

        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("Token is null !!");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        log.info("Authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String access = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        //토큰 만료시 refresh 토큰을 이용해 새로운 토큰 발급
        //리팩토링 필요
        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {

            log.info("Token is expired");
            String newToken = republishingToken(access);
            if(StringUtils.hasText(newToken)){
                access = newToken;
            }else {
                response.getWriter().print("access token is expired");
                //모든 토큰 만료시 401 return
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }


        //토큰에서 username ,role 획득
        String username = jwtUtil.getUserName(access);
        String role = username.equals("ADMIN")?"ADMIN":"USER";


        //userEntity를 생성하여 값 set
        MemberEntity memberEntity = MemberEntity.builder()
                .password("temppassword")
                .username(username)
                .build();


        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(memberEntity,role);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

    public String republishingToken(String access){

        return refreshTokenService.republishAccessToken(access);
    }

}
