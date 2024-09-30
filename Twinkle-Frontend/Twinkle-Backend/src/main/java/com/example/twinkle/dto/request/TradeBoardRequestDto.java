package com.example.twinkle.dto.request;


import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.TradeBoardEntity;
import lombok.Data;

@Data
public class TradeBoardRequestDto {

    private String title;

    private String content;

    private String nickname;

    private String category;

    private Integer price;

    public static TradeBoardEntity toEntity(String nickname, MemberEntity member, String content,
                                     String title, String category, Integer price ){

        TradeBoardEntity tradeBoardEntity = TradeBoardEntity.builder()
                .member(member)
                .nickname(nickname)
                .category(category)
                .price(price)
                .title(title)
                .content(content)
                .build();

        return tradeBoardEntity;
    }

}
