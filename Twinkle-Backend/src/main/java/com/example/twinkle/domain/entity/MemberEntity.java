package com.example.twinkle.domain.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="member")
@Getter
@RequiredArgsConstructor
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String password;

    @Builder
    public MemberEntity(Long id,String username,String nickname,String email,String password){
        this.id=id;
        this.username=username;
        this.nickname=nickname;
        this.email=email;
        this.password=password;
    }

}
