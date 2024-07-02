package com.example.twinkle.service.tradeboard;


import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.TradeBoardEntity;
import com.example.twinkle.dto.request.TradeBoardRequestDto;
import com.example.twinkle.dto.response.TradeBoardResponseDto;
import com.example.twinkle.repository.MemberRepository;
import com.example.twinkle.repository.tradeboard.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeBoardService {

    private final TradeBoardRepository tradeBoardRepository;
    private final MemberRepository memberRepository;

    /**
     * 중고거래 게시물 저장
     */
    @Transactional
    public Long saveTradeBoardPost(TradeBoardRequestDto tradeBoardRequestDto) {

        MemberEntity member = memberRepository.findByNickname(tradeBoardRequestDto.getNickname())
                .orElseThrow(ErrorCode::throwMeberNotFound);

        TradeBoardEntity tradeBoardEntity = TradeBoardRequestDto.toEntity(
                tradeBoardRequestDto.getNickname(),member,tradeBoardRequestDto.getContent(),tradeBoardRequestDto.getTitle(),
                tradeBoardRequestDto.getCategory(),tradeBoardRequestDto.getPrice()
        );

        Long id = tradeBoardRepository.save(tradeBoardEntity).getId();

        return id;
    }

    /***
      중고거래 게시물 단건 조회
     * @param id
     */
    public TradeBoardResponseDto findById(Long id) {
        TradeBoardEntity tradeBoardEntity = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwPostNotFound);

        return TradeBoardResponseDto.toDto(tradeBoardEntity);
    }
}
