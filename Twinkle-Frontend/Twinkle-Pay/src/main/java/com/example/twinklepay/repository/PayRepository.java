package com.example.twinklepay.repository;

import com.example.twinklepay.domain.entity.PayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayRepository extends JpaRepository<PayEntity,Long> {
    Optional<PayEntity> findByMemberId(Long memberId);
}
