package com.example.twinkle.repository.tradeboard;

import com.example.twinkle.domain.entity.TradeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TradeBoardRepository extends JpaRepository<TradeBoardEntity,Long>,CustomTradeBoardRepository {

    Optional<List<TradeBoardEntity>> findAllByNicknameOrderByIdDesc(String nickname);
}
