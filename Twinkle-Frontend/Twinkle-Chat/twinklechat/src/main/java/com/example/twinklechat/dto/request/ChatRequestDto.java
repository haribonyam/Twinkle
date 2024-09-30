package com.example.twinklechat.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChatRequestDto {

    @NotNull
    private String nickname;
    @NotNull
    private Long memberId;

}
