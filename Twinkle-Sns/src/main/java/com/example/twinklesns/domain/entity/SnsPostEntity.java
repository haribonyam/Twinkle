package com.example.twinklesns.domain.entity;


import com.example.twinklesns.dto.request.SnsPostRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name="post")
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsPostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String nickname;;

    private String category;

    private String title;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition ="Integer default 0", nullable = false)
    private Integer viewCount;

    @Column(columnDefinition ="Integer default 0", nullable = false)
    private Integer likeCount;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "snsPost",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private List<FileEntity> files = new ArrayList<>();
//
//    @OneToMany(mappedBy = "snsPost",cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
//    @BatchSize(size = 100)
//    private List<CommentEntity> comments = new ArrayList<>();

    @Builder
    public SnsPostEntity(Long memberId,String category,String title,String content,String nickname,Integer viewCount,Integer likeCount){
        this.memberId=memberId;
        this.category=category;
        this.title=title;
        this.content=content;
        this.nickname=nickname;
        this.createdDate = LocalDateTime.now();
        this.likeCount=likeCount;
        this.viewCount=viewCount;
    }

    /**
     * 양방향 맵핑 post-file
     */
    public void  addFile(FileEntity file){
        this.files.add(file);
        file.addPost(this);
    }

//    public void addComment(CommentEntity comment){
//        this.comments.add(comment);
//        comment.addPost(this);
//    }

    public void updatePost(SnsPostRequestDto snsPostRequestDto){
        this.title = snsPostRequestDto.getTitle();
        this.content = snsPostRequestDto.getContent();
    }

    public void viewCountUp(){
        this.viewCount++;
    }
    public void goodCountUp(){
        this.likeCount++;
    }
    public void goodCountDown(){
        this.likeCount--;
    }
}
