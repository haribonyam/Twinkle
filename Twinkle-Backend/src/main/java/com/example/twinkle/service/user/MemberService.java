package com.example.twinkle.service.user;


import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.dto.request.MemberRequestDto;
import com.example.twinkle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long joinUser(MemberRequestDto memberRequestDto){
        try {
            memberRequestDto.setPassword(bCryptPasswordEncoder.encode(memberRequestDto.getPassword()));
            MemberEntity memberEntity = memberRequestDto.toEntity();

            return memberRepository.save(memberEntity).getId();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
        }
}
