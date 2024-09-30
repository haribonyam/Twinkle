package com.example.twinklesns.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    FILE_MAX_SIZE(PAYLOAD_TOO_LARGE, "허용된 이미지 크기를 초과했습니다.(3MB)", "009"),
    SNS_POST_NOT_FOUND(NOT_FOUND,"게시물을 찾을 수 없습니다.","011"),
    SNS_COMMENT_NOT_FOUND(NOT_FOUND,"해당 댓글 정보를 찾을 수 없습니다.","020"),
    SNS_LIKE_DUPLICATE(CONFLICT,"이미 좋아요 누른 게시물 입니다.","021");

    private final HttpStatus httpStatus;
    private final String detail;
    private final String errorCode;

    public static CustomException throwFileMaxSize(){
        throw new CustomException(FILE_MAX_SIZE);
    }
    public static CustomException throwSnsPostNotFound(){
        throw new CustomException(SNS_POST_NOT_FOUND);
    }
    public static CustomException throwSnsCommentNotFound(){throw new CustomException(SNS_COMMENT_NOT_FOUND);}
    public static CustomException throwSnsLikeDuplicate(){throw new CustomException(SNS_LIKE_DUPLICATE);}
}