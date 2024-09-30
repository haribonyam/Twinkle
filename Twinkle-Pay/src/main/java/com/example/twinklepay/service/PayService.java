package com.example.twinklepay.service;

import com.example.twinklepay.common.exception.ErrorCode;
import com.example.twinklepay.domain.entity.PayEntity;
import com.example.twinklepay.dto.request.PayRequestDto;
import com.example.twinklepay.dto.response.PayResponseDto;
import com.example.twinklepay.repository.PayRepository;
import com.example.twinklepay.service.kafka.SoldOutProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayService {

    private final PayRepository payRepository;
    private final SoldOutProducer soldOutProducer;

    @Transactional
    public PayResponseDto chargePayMoney(PayRequestDto payRequestDto){
        Optional<PayEntity> payEntity = payRepository.findByMemberId(payRequestDto.getMemberId());
        if(payEntity.isPresent()){
            PayEntity pay = payEntity.get();
            pay.increasePayMoney(payRequestDto.getPayMoney());
        }else{
            payRepository.save(
                    PayEntity.builder()
                            .payMoney(payRequestDto.getPayMoney())
                            .memberId(payRequestDto.getMemberId())
                            .build()
            );
        }

        return PayResponseDto.builder()
                .payMoney(payRequestDto.getPayMoney())
                .memberId(payRequestDto.getMemberId())
                .build();
    }

    @Transactional
    public PayResponseDto getPayMoney(Long memberId) {
        Optional<PayEntity> payEntity = payRepository.findByMemberId(memberId);
        if(payEntity.isPresent()){
            PayEntity pay = payEntity.get();
            return PayResponseDto.builder()
                    .payMoney(pay.getPayMoney())
                    .memberId(pay.getMemberId())
                    .build();
        }else{
            ErrorCode.throwPayMoneyNotFount();
        }
        return null;
    }

    @Transactional
    public PayResponseDto tradePayMoney(PayRequestDto payRequestDto) {
        Optional<PayEntity> sellerEntity = payRepository.findByMemberId(payRequestDto.getSellerId());
        Optional<PayEntity> buyerEntity = payRepository.findByMemberId(payRequestDto.getMemberId());
        PayEntity buyer = buyerEntity.get();

        if(buyer.getPayMoney() < payRequestDto.getPayMoney()){
            throw ErrorCode.throwNotEnoughMoney();
        }

        if(!sellerEntity.isPresent()){
            payRepository.save(PayEntity.builder()
                    .memberId(payRequestDto.getSellerId())
                    .payMoney(payRequestDto.getPayMoney())
                    .build());
            buyer.decreasePayMoney(payRequestDto.getPayMoney());
        }else{
            PayEntity seller = sellerEntity.get();
            buyer.decreasePayMoney(payRequestDto.getPayMoney());
            seller.increasePayMoney(payRequestDto.getPayMoney());
        }
        log.info("tradeBoardId : {}",payRequestDto.getTradeBoardId());
        soldOutProducer.send("soldOut", payRequestDto.getTradeBoardId());

        return PayResponseDto.builder()
                .payMoney(payRequestDto.getPayMoney())
                .memberId(payRequestDto.getMemberId())
                .build();
    }
}
