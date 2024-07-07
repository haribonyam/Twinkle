package com.example.twinkle.repository.tradeboard;

import com.example.twinkle.dto.response.TradeBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CustomTradeBoardRepository {

    public Page<TradeBoardResponseDto> findAllDynamic(Map<String, String> cond, Pageable pageable);
}
