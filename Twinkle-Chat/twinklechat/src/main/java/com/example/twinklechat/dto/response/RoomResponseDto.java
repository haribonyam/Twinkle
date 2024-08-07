package com.example.twinklechat.dto.response;

import com.example.twinklechat.domain.entity.RoomEntity;
import com.example.twinklechat.dto.chat.Message;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class RoomResponseDto {

    private Long roomId;

    private Long createMember;

    private Long joinMember;

    private String nickname;

    private String roomName;

    private String createdDate;

    private String thumbNail;

    private TradeBoardResponseDto tradeBoard;

    private List<Message> chattings;


    @Builder
    public RoomResponseDto(RoomEntity room){
        this.roomId=room.getRoomId();
        this.createdDate =room.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
        this.joinMember=room.getJoinMemberId();
        this.nickname = room.getNickname();
        this.createMember=room.getCreateMemberId();
        this.roomName =room.getRoomName();
        this.thumbNail=room.getThumbNail();
    }

    public void addHistory(List<Message> chattings){
        this.chattings = chattings;
    }

    public void addTradeBoard(TradeBoardResponseDto tradeBoard){
        this.tradeBoard = tradeBoard;
    }
}
