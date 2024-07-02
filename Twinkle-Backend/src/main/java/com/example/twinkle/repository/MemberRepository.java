package com.example.twinkle.repository;

import com.example.twinkle.domain.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository  extends JpaRepository<MemberEntity,Long> {


    MemberEntity findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    Optional<MemberEntity> findByNickname(String nickname);
}
