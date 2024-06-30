package com.example.twinkle.service.user;

import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.dto.CustomUserDetails;
import com.example.twinkle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity userData = memberRepository.findByUsername(username);

        if (userData == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        String role = "USER";
        if(userData.getUsername().equals("ADMIN")) role ="ADMIN";

        return new CustomUserDetails(userData,role);
    }
}