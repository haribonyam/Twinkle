package com.example.twinklepay.dto.response;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class PayResponseDto {
    @NotNull
    private Integer payMoney;
    @NotNull
    private Long memberId;

    @Builder
    public PayResponseDto(Integer payMoney,Long memberId){
        this.payMoney = payMoney;
        this.memberId = memberId;
    }
}
