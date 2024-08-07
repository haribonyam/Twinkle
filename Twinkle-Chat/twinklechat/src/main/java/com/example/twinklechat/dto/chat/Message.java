package com.example.twinklechat.dto.chat;

import com.example.twinklechat.domain.mongo.Chatting;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String id;
    @NotNull
    private Long roomId;
    @NotNull
    private String content;
    private Long senderId;
    private String sender;
    private Long tradeBoardId;
    private Long sendTime; //id로 구분할 수 없음 sort 용도

    public void setId(String id){
        this.id = id;
    }

    public void setTime(LocalDateTime sendTime){
            this.sendTime = sendTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
    }

    @Builder
    public Message(Chatting chatting){
        this.roomId = chatting.getRoomId();
        this.sender = chatting.getSender();
        this.sendTime = chatting.getSendTime();
        this.senderId = chatting.getSenderId();
        this.content = chatting.getContent();
        this.id = chatting.getId();
    }

    public Chatting toChattingEntity(){
        return Chatting.builder()
                .content(this.content)
                .roomId(this.roomId)
                .senderId(this.senderId)
                .sender(this.sender)
                .build();
    }

}
