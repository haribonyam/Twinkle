package com.example.twinklepay.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SoldOutProducer {

    private KafkaTemplate<String, Long> kafkaTemplate;
    @Autowired
    public SoldOutProducer(KafkaTemplate<String,Long> kafkaTemplate){
        this.kafkaTemplate = kafkaTemplate;
    }
    public void send(String topic,Long tradeBoardId){
        kafkaTemplate.send(topic,tradeBoardId);
    }
}
