package com.example.twinklesns.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="file")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private SnsPostEntity snsPost;

    private String path;

    private String name;

    @Builder
    public FileEntity(String path, String name,SnsPostEntity snsPostEntity){
        this.snsPost =snsPostEntity;
        this.path =path;
        this.name =name;
    }

    /**
     * 양방향 맵핑 file-post
     */
    public void addPost(SnsPostEntity snsPostEntity){
        this.snsPost = snsPostEntity;
    }
}
