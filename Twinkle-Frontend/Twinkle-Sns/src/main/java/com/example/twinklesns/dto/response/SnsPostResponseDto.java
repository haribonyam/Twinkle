package com.example.twinklesns.dto.response;

import com.example.twinklesns.domain.entity.SnsPostEntity;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SnsPostResponseDto {

    private Long id;
    private Long memberId;
    private String category;
    private String nickname;
    private String title;
    private String content;
    private String createdDate;
    private Integer viewCount;
    private Integer likeCount;
    private List<String> images;

    @Builder
    public SnsPostResponseDto(SnsPostEntity snsPostEntity){
        List<String> images = snsPostEntity.getFiles().stream().map(image->image.getPath()).collect(Collectors.toList());
        this.id = snsPostEntity.getId();
        this.memberId=snsPostEntity.getMemberId();
        this.category=snsPostEntity.getCategory();
        this.nickname=snsPostEntity.getNickname();
        this.createdDate=snsPostEntity.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.title=snsPostEntity.getTitle();
        this.content=snsPostEntity.getContent();
        this.viewCount=snsPostEntity.getViewCount();
        this.likeCount=snsPostEntity.getLikeCount();
        this.images=images;
    }


}
