package com.example.twinklesns.dto.response;

import com.example.twinklesns.domain.entity.CommentEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private List<CommentResponseDto> comments;
    private Long commentCount;
    private Boolean isLiked;
    @Builder
    public SnsPostResponseDto(SnsPostEntity snsPostEntity, Long commentCount, Boolean isLiked, List<CommentEntity> commentsList){
        List<String> images = snsPostEntity.getFiles().stream().map(image->image.getPath()).collect(Collectors.toList());
        if (commentsList == null) {
            commentsList = new ArrayList<>();
        }

        // commentsList를 처리하면서 부모가 없는 댓글만 필터링
        List<CommentResponseDto> comments = commentsList.stream()
                .filter(comment -> comment != null && comment.getParent() == null) // comment가 null이 아닌지 체크 후 getParent() 호출
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());

        this.comments = comments;
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
        this.commentCount=commentCount;
        this.isLiked=isLiked;
    }
    @Builder
    public SnsPostResponseDto(SnsPostEntity snsPostEntity, Long commentCount) {
        this(snsPostEntity, commentCount, false, new ArrayList<>());  // 기본값으로 설정
    }

}
