package com.example.twinklechat.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Table(name="room")
@DynamicInsert
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    private Long createMemberId;

    private Long joinMemberId;

    private Long tradeBoardId;

    private String nickname;

    private String roomName;

    private LocalDateTime createdDate;

    private String thumbNail;

    @PrePersist
    public void createdDate(){
        this.createdDate = LocalDateTime.now();
    }

    @Builder
    public RoomEntity(Long createMemberId,String thumbNail,Long tradeBoardId,String nickname,Long joinMemberId, String roomName){
        this.createMemberId = createMemberId;
        this.tradeBoardId = tradeBoardId;
        this.nickname = nickname;
        this.thumbNail = thumbNail;
        this.joinMemberId = joinMemberId;
        this.roomName = roomName;
    }

}
