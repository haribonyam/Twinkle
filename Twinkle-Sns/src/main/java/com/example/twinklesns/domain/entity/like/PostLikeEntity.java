package com.example.twinklesns.domain.entity.like;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post_like")
public class PostLikeEntity {

    @EmbeddedId
    private PostLikeId id;


    @Builder
    public PostLikeEntity(PostLikeId id){
        this.id = id;
    }
}
