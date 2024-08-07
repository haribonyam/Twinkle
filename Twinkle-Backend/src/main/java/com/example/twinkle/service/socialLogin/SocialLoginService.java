package com.example.twinkle.service.socialLogin;

import com.example.twinkle.common.config.auth.JwtUtil;
import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.status.SocialLogin;
import com.example.twinkle.repository.MemberRepository;
import com.example.twinkle.service.user.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class SocialLoginService extends DefaultOAuth2UserService{

    private final MemberRepository memberRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        Map<String,Object> attribute = oAuth2User.getAttributes();

        try{
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User.getAttributes()));
        }catch(Exception e){
            e.printStackTrace();
        }
        String userId = null;
        String email = null;
        String nickname = null;
        SocialLogin socialLogin = null;

        if(registrationId.equals("kakao")){
            Map<String,Object> properties = (Map<String, Object>) attribute.get("properties");

            userId = registrationId+"_"+attribute.get("id");
            nickname =registrationId+"_"+properties.get("nickname");
            socialLogin = SocialLogin.KAKAO;
            email = "kakao@email.com";
        }

        MemberEntity memberData = memberRepository.findByUsername(userId).orElseThrow(ErrorCode::throwMemberNotFound);

        if(memberData == null){
            MemberEntity socialMember = MemberEntity.builder()
                    .nickname(nickname)
                    .username(userId)
                    .email(email)
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .socialLogin(socialLogin)
                    .build();
            memberRepository.save(socialMember);
            log.info("new socialMember generating!!");
        }
        else{
            memberData.builder()
                    .nickname(nickname)
                    .email(email)
                    .build();
            memberRepository.save(memberData);
            log.info("social member updating!!");
        }

        return new CustomOAuth2User(userId,nickname);
    }


}
