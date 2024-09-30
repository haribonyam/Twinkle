package com.example.twinklesns.dto.request;

import lombok.Data;

@Data
public class SnsCommentRequestDto {

    private String nickname;
    private String content;
    private Long parentId;
    private Boolean isDelete;

}
