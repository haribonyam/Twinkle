package com.example.twinklesns.domain.entity;

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
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String content;

    private Long postId;

    // 닉네임 수정 시 feign으로 여기도 수정해주기
    private String nickname;

    private Boolean isDelete;

    private LocalDateTime createdDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "post_id")
//    private SnsPostEntity snsPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parent;

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
    @BatchSize(size=100)
    private List<CommentEntity> children = new ArrayList<>();

    @PrePersist
    protected void onPrePersist() {
        this.createdDate = LocalDateTime.now();
        this.isDelete = false;
    }

    @Builder
    public CommentEntity(Long postId,String nickname, String content, SnsPostEntity snsPost) {
        this.nickname = nickname;
        this.postId = postId;
        this.content = content;
//        this.snsPost = snsPost;
    }
//
//    public void addPost(SnsPostEntity snsPost) {
//        this.snsPost = snsPost;
//    }

    public void addChild(CommentEntity child) {
        this.children.add(child);
        addParent(this);
    }

    public void addParent(CommentEntity parent) {
        this.parent = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void deleteComment(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
