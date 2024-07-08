package com.example.twinkle.controller.user;


import com.example.twinkle.dto.request.MemberRequestDto;
import com.example.twinkle.dto.response.MemberResponseDto;
import com.example.twinkle.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
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

    /***
     * 로그인한 회원 정보 닉네임으로 조회
     * @param nickname
     * @return
     */
    @GetMapping("/user/{nickname}")
    public ResponseEntity<MemberResponseDto> findByNickname(@PathVariable String nickname){

        return ResponseEntity.ok(memberService.findByNickname(nickname));
    }

}
