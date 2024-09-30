package com.example.twinkle.repository.user;

import com.example.twinkle.domain.redis.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByAccessToken(String access);

    Optional<Object> findByUsername(String username);
}
