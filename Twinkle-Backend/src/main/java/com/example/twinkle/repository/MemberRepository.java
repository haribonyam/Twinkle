package com.example.twinkle.repository;

import com.example.twinkle.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository  extends JpaRepository<MemberEntity,Long> {


    MemberEntity findByUsername(String username);
}
