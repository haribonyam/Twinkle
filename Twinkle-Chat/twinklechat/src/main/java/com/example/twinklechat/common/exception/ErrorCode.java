package com.example.twinklechat.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;
@Getter
@AllArgsConstructor
public enum ErrorCode {

    MEMBER_NOT_FOUND(NOT_FOUND, "회원 정보를 찾을 수 없습니다.", "007"),
    POST_NOT_FOUND(NOT_FOUND, "게시물 정보를 찾을 수 없습니다.", "008"),
    TOKEN_IS_EXPIRED(UNAUTHORIZED,"토큰이 만료되었습니다.","015"),
    UNAUTHORIZED_ACCESS(FORBIDDEN,"권한이 없는 접근 경로 입니다.","012"),
    CHATROOM_NOT_FOUND(NOT_FOUND,"채팅방 정보를 찾을 수 없습니다.","013"),
    CHATROOM_ALREADY_EXIST(CONFLICT,"채팅방이 이미 존재합니다.","014");

    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;


    public static CustomException throwMemberNotFound(){

        throw new CustomException(MEMBER_NOT_FOUND);
    }

    public static CustomException throwSnsPostNotFound(){

        throw new CustomException(POST_NOT_FOUND);
    }
    public static CustomException throwUnauthorizedAccess(){

        throw new CustomException(UNAUTHORIZED_ACCESS);
    }

    public static CustomException throwChatroomNotFound()
    {
        throw new CustomException(CHATROOM_NOT_FOUND);

    }
    public static CustomException throwTokenIsExpired(){
        throw new CustomException(TOKEN_IS_EXPIRED);
    }

    public static CustomException throwChatRoomAlreadyExist(){
        throw new CustomException(CHATROOM_ALREADY_EXIST);
    }
}
