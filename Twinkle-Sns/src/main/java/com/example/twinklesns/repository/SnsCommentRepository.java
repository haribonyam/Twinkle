package com.example.twinklesns.repository;

import com.example.twinklesns.domain.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SnsCommentRepository extends JpaRepository<CommentEntity,Long> {


    List<CommentEntity> findCommentsByPostId(Long postId);
}
