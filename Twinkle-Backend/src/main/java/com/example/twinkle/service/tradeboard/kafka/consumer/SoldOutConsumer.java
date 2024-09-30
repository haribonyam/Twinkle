package com.example.twinkle.service.tradeboard.kafka.consumer;

import com.example.twinkle.service.tradeboard.TradeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SoldOutConsumer {

    private final TradeBoardService tradeBoardService;

    @KafkaListener(topics = "soldOut", containerFactory = "kafkaSoldOutPostContainerFactory")
    public void soldOut(String kafkaMessage, Long tradeBoardId) {
        log.info("Kafka Message : -> {}",kafkaMessage);
        if (tradeBoardId == null) {
            throw new IllegalArgumentException("TradeBoard ID is missing in the Kafka message");
        }
        tradeBoardService.requestTrade(tradeBoardId,null);

    }
}
