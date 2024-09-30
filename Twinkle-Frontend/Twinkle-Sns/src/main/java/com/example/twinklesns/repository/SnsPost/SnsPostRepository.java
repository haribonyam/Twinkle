package com.example.twinklesns.repository.SnsPost;

import com.example.twinklesns.domain.entity.SnsPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SnsPostRepository extends JpaRepository<SnsPostEntity,Long>,CustomSnsPostRepository {

    @Query("SELECT p FROM SnsPostEntity p JOIN FETCH p.files WHERE p.id = :id")
    Optional<SnsPostEntity> findPostById(@Param("id") Long id);
}
