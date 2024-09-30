package com.example.twinkle.controller.user;


import com.example.twinkle.dto.request.MemberRequestDto;
import com.example.twinkle.dto.response.MemberResponseDto;
import com.example.twinkle.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {
        private final MemberService memberService;

    @PostMapping("/user/save")
    public ResponseEntity<Long> saveUser(@RequestBody MemberRequestDto memberRequestDto){
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/user/save").toUriString()
        );
        return ResponseEntity.created(uri).body(memberService.joinUser(memberRequestDto));
    }

    /**
     * 회원가입 아이디 중복확인
     */
    @GetMapping("/user/check/username")
    public ResponseEntity<?> checkUserId(@RequestParam String username){

        HttpStatus status = memberService.duplicatedUsername(username);

        return new ResponseEntity<>(status);
    }

    /**
     * 회원가입 닉네임 중복확인
     */
    @GetMapping("/user/check/nickname")
    public ResponseEntity<?> checkUserNickname(@RequestParam String nickname){

        HttpStatus status = memberService.duplicatedNickname(nickname);

        return new ResponseEntity<>(status);
    }

    /**
     * 회원가입 이메일 중복확인
     */
    @GetMapping("/user/check/email")
    public ResponseEntity<?> checkUserEmail(@RequestParam String email){

        HttpStatus status = memberService.duplicatedEmail(email);

        return new ResponseEntity<>(status);
    }
    @GetMapping("/user/check/{memberId}")
    public ResponseEntity<String> validationMember(@PathVariable Long memberId,@RequestHeader("Authorization") String jwt){
        String nickname = memberService.userValidation(jwt,memberId);
        return ResponseEntity.ok(nickname);
    }

    @GetMapping("/user/{nickname}")
    public ResponseEntity<MemberResponseDto> findByNickname(@PathVariable String nickname){
        log.info("approaching user info {}",nickname);
        return ResponseEntity.ok(memberService.findByNickname(nickname));
    }

    @GetMapping("/service/{username}")
    public ResponseEntity<MemberResponseDto> findByUsername(@PathVariable String username){
        log.info("other service reqeust !!");
        return ResponseEntity.ok(memberService.findByUsername(username));
    }
}
