package com.example.twinkle.dto.request;


import com.example.twinkle.domain.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    private String username;
    private String nickname;
    private String password;
    private String email;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .build();
    }

}
