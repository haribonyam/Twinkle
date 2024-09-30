package com.example.twinklesns.dto.request;

import lombok.Data;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

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
    private List<File> images;

}
