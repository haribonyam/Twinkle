package com.example.twinklepay.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum ErrorCode {

    PAY_MONEY_NOT_FOUND(NOT_FOUND,"페이머니가 존재하지 않습니다.","016"),
    NOT_ENOUGH_MONEY(BAD_REQUEST,"페이머니가 부족합니다.","019");


    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;

    public static CustomException throwPayMoneyNotFount(){
        throw new CustomException(PAY_MONEY_NOT_FOUND);
    }

    public static CustomException throwNotEnoughMoney(){throw new CustomException(NOT_ENOUGH_MONEY);}
}
