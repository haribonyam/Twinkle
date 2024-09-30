package com.example.twinklechat.domain.mongo;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Document(collection = "chatting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Chatting {
    @Id
    private String id;
    private Long roomId;
    private Long senderId;
    private String sender;
    private String content;
    private Long sendTime;

    @Builder
    public Chatting(Long roomId,Long senderId,String content,String sender){
        this.roomId = roomId;
        this.senderId = senderId;
        this.sender = sender;
        this.content = content;
        this.sendTime = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }
    @PrePersist
    public void setId(){
        this.id = UUID.randomUUID().toString();
    }
}
