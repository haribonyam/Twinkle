package com.example.twinkle.domain.redis;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/***
 * 현재는 mysql에 저장하고 후에 redis에 저장
 */
@Entity
@Table(name="refresh")
@Data
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;
    private String refreshToken;
    private String username;

    @Builder
    public RefreshToken(String accessToken,String refreshToken,String username){
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
        this.username=username;
    }

    public void updateAccessToken(String accessToken){
        this.accessToken = accessToken;
    }
}
