package com.example.twinkle.controller.user;


import com.example.twinkle.dto.request.MemberRequestDto;
import com.example.twinkle.service.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}
