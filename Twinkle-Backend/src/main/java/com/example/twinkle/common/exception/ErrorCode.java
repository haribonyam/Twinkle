package com.example.twinkle.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum ErrorCode {


    USER_DUPLICATED_ID(CONFLICT,"이미 가입된 아이디 입니다.","004"),
    USER_DUPLICATED_NICKNAME(CONFLICT,"이미 가입된 닉네임 입니다.","005"),
    USER_DUPLICATED_EMAIL(CONFLICT,"이미 가입된 이메일 입니다.","006");

    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;

    public static CustomException throwUserDuplicatedId() {
        throw new CustomException(USER_DUPLICATED_ID);
    }

    public static CustomException throwUserDuplicatedNickname(){
        throw new CustomException(USER_DUPLICATED_NICKNAME);
    }

    public static CustomException throwUserDuplicatedEmail(){
        throw new CustomException(USER_DUPLICATED_EMAIL);
    }
}
