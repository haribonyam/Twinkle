package com.example.twinkle.domain.entity;


import com.example.twinkle.domain.entity.status.SocialLogin;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    @Builder
    public MemberEntity(Long id,String username,String nickname,String email,String password,
                        SocialLogin socialLogin){
        this.id=id;
        this.username=username;
        this.nickname=nickname;
        this.email=email;
        this.password=password;
        this.socialLogin=socialLogin;
    }


}
