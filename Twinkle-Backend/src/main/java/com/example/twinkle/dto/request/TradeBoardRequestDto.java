package com.example.twinkle.dto.request;


import com.example.twinkle.domain.entity.TradeBoardEntity;
import lombok.Data;

@Data
public class TradeBoardRequestDto {

    private String title;

    private String content;

    private String nickname;

    private String category;

    private Integer price;

}
