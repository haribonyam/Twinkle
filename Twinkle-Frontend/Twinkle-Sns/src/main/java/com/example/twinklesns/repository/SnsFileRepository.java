package com.example.twinklesns.repository;

import com.example.twinklesns.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SnsFileRepository extends JpaRepository<FileEntity,Long> {

}
