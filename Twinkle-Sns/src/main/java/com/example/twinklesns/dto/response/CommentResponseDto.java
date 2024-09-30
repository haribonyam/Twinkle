package com.example.twinklesns.dto.response;

import com.example.twinklesns.domain.entity.CommentEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentResponseDto {
    private Long id;
    private String content;
    private String nickname;
    private Boolean isDelete;
    private LocalDateTime createdDate;
    private List<CommentResponseDto> children;
    private Long postId;

    @Builder
    public CommentResponseDto(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.postId = commentEntity.getPostId();
        this.isDelete = commentEntity.getIsDelete();
        this.content = commentEntity.getContent();
        this.createdDate = commentEntity.getCreatedDate();
        this.nickname = commentEntity.getNickname();
        this.children = commentEntity.getChildren().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

}
