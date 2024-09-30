package com.example.twinklepay.dto.request;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
public class PayRequestDto implements Serializable {
    @NotNull
    private Integer payMoney;
    @NotNull
    private Long memberId;

    private Long sellerId;

    private Integer couponMoney;


}
