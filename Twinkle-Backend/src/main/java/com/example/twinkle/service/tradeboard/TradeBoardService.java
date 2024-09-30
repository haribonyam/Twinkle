package com.example.twinkle.service.tradeboard;


import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.domain.entity.MemberEntity;
import com.example.twinkle.domain.entity.TradeBoardEntity;
import com.example.twinkle.domain.entity.status.Condition;
import com.example.twinkle.dto.request.TradeBoardRequestDto;
import com.example.twinkle.dto.response.TradeBoardResponseDto;
import com.example.twinkle.repository.MemberRepository;
import com.example.twinkle.repository.tradeboard.TradeBoardRepository;
import com.example.twinkle.service.tradeboard.kafka.producer.DeleteTradeBoardProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradeBoardService {

    private final TradeBoardRepository tradeBoardRepository;
    private final MemberRepository memberRepository;
    private final FileService fileSerivce;
    private final DeleteTradeBoardProducer deleteTradeBoardProducer;

    /**
     * 중고거래 게시물 저장
     */
    @Transactional
    public Long saveTradeBoardPost(TradeBoardRequestDto tradeBoardRequestDto, List<MultipartFile> files){

        MemberEntity member = memberRepository.findByNickname(tradeBoardRequestDto.getNickname())
                .orElseThrow(ErrorCode::throwMemberNotFound);

        TradeBoardEntity tradeBoardEntity = TradeBoardRequestDto.toEntity(
                tradeBoardRequestDto.getNickname(),member,tradeBoardRequestDto.getContent(),tradeBoardRequestDto.getTitle(),
                tradeBoardRequestDto.getCategory(),tradeBoardRequestDto.getPrice()
        );

        Long id = tradeBoardRepository.save(tradeBoardEntity).getId();

        if(!files.isEmpty()){
            fileSerivce.saveFile(files,tradeBoardEntity);
        }

        return id;
    }

    /***
      중고거래 게시물 단건 조회
     * @param id
     */
    @Transactional
    public TradeBoardResponseDto findById(Long id) {
        TradeBoardEntity tradeBoardEntity = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwPostNotFound);
        tradeBoardEntity.viewCountUp();
        return TradeBoardResponseDto.toDto(tradeBoardEntity);
    }
    @Transactional
    public Page<TradeBoardResponseDto> tradeBoardAllList(Pageable pageable, Map<String,String> searchCondition) {

        return tradeBoardRepository.findAllDynamic(searchCondition,pageable);
    }
    @Transactional
    public void deletePost(Long id) {
       TradeBoardEntity tradeBoard = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwPostNotFound);

       tradeBoardRepository.delete(tradeBoard);

       deleteTradeBoardProducer.send("deletePost",id);
    }

    @Transactional
    public void updatePost(Long id, TradeBoardRequestDto tradeBoardRequestDto) {
        Optional<TradeBoardEntity> optionalTradeBoardEntity = tradeBoardRepository.findById(id);
        if(optionalTradeBoardEntity.isPresent()){
            optionalTradeBoardEntity.get().updatePost(tradeBoardRequestDto.getTitle(),
                    tradeBoardRequestDto.getContent(),tradeBoardRequestDto.getPrice());
        }else{
            ErrorCode.throwPostNotFound();
        }
    }

    /***
     * 거래요청(약속)
     * @param tradeBoardId
     * @param buyerId
     * @return
     */
    @Transactional
    public void requestTrade(Long tradeBoardId, Long buyerId) {
        TradeBoardEntity tradeBoard =tradeBoardRepository.findById(tradeBoardId).orElseThrow(ErrorCode::throwPostNotFound);

        if(buyerId ==null){
            tradeBoard.updateCondition(Condition.판매완료);
        }else{
            tradeBoard.updateCondition(Condition.예약중);
            tradeBoard.setBuyer(buyerId);
        }
    }

    @Transactional
    public List<TradeBoardResponseDto> findAllByNickname(String nickname) {
        List<TradeBoardEntity> entities = tradeBoardRepository.findAllByNicknameOrderByIdDesc(nickname).orElseThrow(ErrorCode::throwPostNotFound);
        return entities.stream()
                .map(TradeBoardResponseDto::toDto)
                .collect(Collectors.toList());
    }
}
