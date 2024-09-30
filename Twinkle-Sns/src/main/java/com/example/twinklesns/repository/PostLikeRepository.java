package com.example.twinklesns.repository;

import com.example.twinklesns.domain.entity.like.PostLikeEntity;
import com.example.twinklesns.domain.entity.like.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, PostLikeId> {

    @Modifying
    @Query("DELETE FROM PostLikeEntity p WHERE p.id.postId = :postId")
    void deleteByPostId(@Param("postId") Long postId);

}
