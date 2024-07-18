package com.example.twinklesns.repository.SnsPost;

import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CustomSnsPostRepository {

    public Page<SnsPostResponseDto> DynamicFindAll(Pageable pageable, Map<String, String> searchCondition);


}
