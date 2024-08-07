package com.example.twinklechat.dto.response;

import lombok.Data;

@Data
public class MemberResponseDto {

    private Long id;
    private String username;
    private String nickname;
    private String email;

}
