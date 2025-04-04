package com.example.twinkle.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_DUPLICATED_ID(CONFLICT, "이미 가입된 아이디입니다.", "004"),
    USER_DUPLICATED_NICKNAME(CONFLICT, "이미 가입된 닉네임입니다.", "005"),
    USER_DUPLICATED_EMAIL(CONFLICT, "이미 가입된 이메일입니다.", "006"),
    MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다.", "007"),
    POST_NOT_FOUND(NOT_FOUND, "게시물 정보를 찾을 수 없습니다.", "008"),
    FILE_MAX_SIZE(PAYLOAD_TOO_LARGE, "허용된 이미지 크기를 초과했습니다.(3MB)", "009"),
    EXTENSION_NOT_ALLOWED(NOT_ACCEPTABLE,"jpg 혹은 png 확장자만 사용 가능합니다.","010"),
    INVALID_TOKEN(UNAUTHORIZED,"토큰정보를 가져올 수 없습니다. 다시 로그인 해주세요.","015");

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

    public static CustomException throwMemberNotFound(){

        throw new CustomException(MEMBER_NOT_FOUND);
    }

    public static CustomException throwPostNotFound(){

        throw new CustomException(POST_NOT_FOUND);
    }
    public static CustomException throwFileMaxSize(){

        throw new CustomException(FILE_MAX_SIZE);
    }
    public static CustomException throwExtensionNotAcceptable(){
      throw new CustomException(EXTENSION_NOT_ALLOWED);
    }

    public static CustomException throwInvalidToken(){
        throw new CustomException(INVALID_TOKEN);
    }
}
