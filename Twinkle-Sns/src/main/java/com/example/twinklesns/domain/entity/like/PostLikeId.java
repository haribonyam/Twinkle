package com.example.twinklesns.domain.entity.like;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
public class PostLikeId implements Serializable {

    private Long postId;
    private Long userId;

    @Builder
    public PostLikeId(Long postId, Long userId){
        this.postId = postId;
        this.userId = userId;
    }

}
