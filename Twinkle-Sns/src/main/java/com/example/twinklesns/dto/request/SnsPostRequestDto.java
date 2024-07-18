package com.example.twinklesns.dto.request;

import lombok.Data;

@Data
public class SnsPostRequestDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String category;
    private Long memberId;
    private Integer viewCount;
    private Integer likeCount;


}
