package com.example.twinklechat.service.kafka.cosumer;

import com.example.twinklechat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DeletePostService {
    private final ChatService chatService;

    @KafkaListener(topics = "deletePost", containerFactory = "kafkaDeletePostContainerFactory")
    public void deleteChat(String kafkaMessage, Long tradeBoardId) {
        log.info("Kafka Message : ->" + kafkaMessage);
        if (tradeBoardId == null) {
            throw new IllegalArgumentException("TradeBoard ID is missing in the Kafka message");
        }
        chatService.deleteChatRoom(tradeBoardId);

    }

}
