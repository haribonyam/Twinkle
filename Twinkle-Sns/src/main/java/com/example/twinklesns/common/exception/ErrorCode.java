package com.example.twinklesns.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    FILE_MAX_SIZE(PAYLOAD_TOO_LARGE, "허용된 이미지 크기를 초과했습니다.(3MB)", "010"),
    POST_NOT_FOUND(NOT_FOUND,"게시물을 찾을 수 없습니다.","011");

    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;

    public static CustomException throwFileMaxSize(){
        throw new CustomException(FILE_MAX_SIZE);
    }
    public static CustomException throwPostNotFound(){
        throw new CustomException(POST_NOT_FOUND);
    }


}