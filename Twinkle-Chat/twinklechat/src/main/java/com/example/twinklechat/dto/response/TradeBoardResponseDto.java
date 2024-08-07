package com.example.twinklechat.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TradeBoardResponseDto {
    private Long id;
    private Long memberId;
    private String nickname;
    private String title;
    private String content;
    private String condition;
    private String createdDate;
    private String updatedDate;
    private String category;
    private Integer view;
    private Integer price;
    private List<String> paths;

}
