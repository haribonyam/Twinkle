package com.example.twinkle.service.socialLogin;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private String username;
    private String nickname;

    public String getUsername(){
        return this.username;
    }

    public String getNickname(){
        return this.nickname;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return "ROLE_USER";
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return username;
    }
}
