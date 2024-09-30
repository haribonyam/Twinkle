package com.example.twinkle.service.user;


import com.example.twinkle.common.config.auth.JwtUtil;
import com.example.twinkle.common.exception.CustomException;
import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.status.SocialLogin;
import com.example.twinkle.dto.request.MemberRequestDto;
import com.example.twinkle.dto.response.MemberResponseDto;
import com.example.twinkle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;

    public Long joinUser(MemberRequestDto memberRequestDto){

        memberRequestDto.setPassword(bCryptPasswordEncoder.encode(memberRequestDto.getPassword()));
        MemberEntity memberEntity = memberRequestDto.toEntity(SocialLogin.NON);

        return memberRepository.save(memberEntity).getId();

        }

    public HttpStatus duplicatedUsername(String username) {
        boolean check = memberRepository.existsByUsername(username);
        if(!check){
            return HttpStatus.OK;
        }else{
            throw ErrorCode.throwUserDuplicatedId();
        }
    }

    public HttpStatus duplicatedNickname(String nickname) {
        boolean check = memberRepository.existsByNickname(nickname);
        if(!check){
            return HttpStatus.OK;
        }else{
            throw ErrorCode.throwUserDuplicatedNickname();
        }
    }

    public HttpStatus duplicatedEmail(String email) {

        boolean check = memberRepository.existsByEmail(email);
        if(!check){
            return HttpStatus.OK;
        }else{
            throw ErrorCode.throwUserDuplicatedEmail();
        }
    }

    public MemberResponseDto findByNickname(String nickname) {
        MemberEntity member = memberRepository.findByNickname(nickname).orElseThrow(ErrorCode::throwMemberNotFound);
        return new MemberResponseDto(member);
    }
    public MemberResponseDto findByUsername(String username){
        MemberEntity member = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);
        return new MemberResponseDto(member);
    }

    /***
     * jwt 정보와 memberId 정보로 교차 검증
     * @param jwt
     * @param memberId
     * @return nickname
     */
    @Transactional
    public String userValidation(String jwt, Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(ErrorCode::throwMemberNotFound);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            String token = jwt.substring(7);
            String username = jwtUtil.getUserName(token);

            if (member.getUsername().equals(username)) {
                return member.getNickname();
            } else {
                ErrorCode.throwMemberNotFound();
            }
        } else {
            ErrorCode.throwInvalidToken();
        }
        return null; }


    public MemberResponseDto findByUserName(String username) {
        MemberEntity member = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);
        return new MemberResponseDto(member);
    }
}
