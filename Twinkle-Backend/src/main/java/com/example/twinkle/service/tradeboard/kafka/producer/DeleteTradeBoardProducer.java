package com.example.twinkle.service.tradeboard.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeleteTradeBoardProducer {
    private KafkaTemplate<String, Long> kafkaTemplate;
    @Autowired
    public DeleteTradeBoardProducer(KafkaTemplate<String,Long> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic,Long tradeBoardId){
        kafkaTemplate.send(topic,tradeBoardId);
    }
}
