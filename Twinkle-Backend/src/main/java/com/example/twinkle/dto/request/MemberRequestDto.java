package com.example.twinkle.dto.request;


import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.status.SocialLogin;
import lombok.*;

@Data
@RequiredArgsConstructor
public class MemberRequestDto {

    private String username;
    private String nickname;
    private String password;
    private String email;

    public MemberEntity toEntity(SocialLogin socialLogin){
        return MemberEntity.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .socialLogin(socialLogin)
                .build();
    }

}
