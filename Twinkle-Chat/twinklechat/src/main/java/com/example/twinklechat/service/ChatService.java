package com.example.twinklechat.service;

import com.example.twinklechat.common.exception.ErrorCode;
import com.example.twinklechat.common.interceptor.JwtHandler;
import com.example.twinklechat.common.util.RoomUtil;
import com.example.twinklechat.domain.entity.RoomEntity;
import com.example.twinklechat.domain.mongo.Chatting;
import com.example.twinklechat.dto.chat.Message;
import com.example.twinklechat.dto.request.ChatRequestDto;
import com.example.twinklechat.dto.response.MemberResponseDto;
import com.example.twinklechat.dto.response.RoomResponseDto;
import com.example.twinklechat.dto.response.TradeBoardResponseDto;
import com.example.twinklechat.repository.ChattingRepository;
import com.example.twinklechat.service.feignClient.TwinkleService;
import com.example.twinklechat.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final TwinkleService twinkleService;
    private final RoomRepository roomRepository;
    private final ChattingRepository chattingRepository;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final JwtHandler jwtHandler;

    @Transactional
    public RoomEntity createChatRoom(ChatRequestDto requestDto, Long tradeBoardId) {
        //feign client로 twinkle-backend 에서 tradeboard post 정보 가져오기
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<TradeBoardResponseDto> tradeBoard = circuitBreaker.run(() ->
                        twinkleService.TradeBoardPost(tradeBoardId),
                throwable -> ResponseEntity.ok(null));

        String thumbNail = null;
        if(tradeBoard.getBody() != null && tradeBoard.getBody().getPaths() !=null){
            thumbNail = tradeBoard.getBody().getPaths().get(0);
        }

        return roomRepository.save(RoomEntity.builder()
                        .createMemberId(requestDto.getMemberId())
                        .joinMemberId(tradeBoard.getBody().getMemberId())
                        .tradeBoardId(tradeBoard.getBody().getId())
                        .nickname(requestDto.getNickname())
                        .thumbNail(thumbNail)
                        .roomName(tradeBoard.getBody().getNickname())
                        .build());
    }

    @Transactional
    public void saveChatting(Message message) {

        chattingRepository.save(message.toChattingEntity());

    }

    @Transactional
    public List<RoomResponseDto> findRoomList(String accessToken) {

        String username =jwtHandler.getUserName(accessToken);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<MemberResponseDto> member = circuitBreaker.run(() ->
                        twinkleService.findByUsername(username),
                throwable -> ResponseEntity.ok(null));

        Long memberId = member.getBody().getId();
        List<RoomEntity> rooms = roomRepository.findAllByMemberId(memberId);

        return rooms.stream()
                .map(RoomResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 채팅방에 들어갔을 때 사용자 인증 + 채팅방 정보 & 채팅 히스토리 response
     * @param accessToken
     * @param roomId
     * @return
     */
    public RoomResponseDto enterRoom(String accessToken, Long roomId) {

        String username = jwtHandler.getUserName(accessToken);
        log.info("{} access to chat room : {}",username,roomId);

        MemberResponseDto member = getMember(username);
        Long memberId = member.getId();

        RoomEntity room = roomRepository.findById(roomId).orElseThrow(ErrorCode::throwChatroomNotFound);

        if(memberId !=room.getJoinMemberId() && memberId != room.getCreateMemberId()){
            log.info("UNAUTHORIZED USER ACCESS TO chat room : {}",roomId);
            ErrorCode.throwUnauthorizedAccess();
        }

        List<Chatting> chattings = chattingRepository.findAllByRoomId(roomId);
        List<Message> messages = chattings.stream()
                .map(Message::new)
                .collect(Collectors.toList());

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<TradeBoardResponseDto> tradeBoard = circuitBreaker.run(() ->
                        twinkleService.TradeBoardPost(room.getTradeBoardId()),
                throwable -> ResponseEntity.ok(null));


        RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                .room(room)
                .build();
        roomResponseDto.addHistory(messages);
        roomResponseDto.addTradeBoard(tradeBoard.getBody());

        return roomResponseDto;
    }

    public Long isExist(String accessToken, Long tradeBoardId) {
        String username = jwtHandler.getUserName(accessToken);
        MemberResponseDto member = getMember(username);
        log.info("access to roomEntity to memberId:{} , tradeBoardId : {}",member.getId(),tradeBoardId);
        RoomEntity room = roomRepository.findByTradeBoardIdAndMemberId(tradeBoardId,member.getId()).orElseThrow(ErrorCode::throwChatroomNotFound);
//        boolean isExist = roomRepository.existsByTradeBoardId(tradeBoardId,member.getId());
//
//        if(isExist){
//            ErrorCode.throwChatRoomAlreadyExist();
//        }

        log.info("return roomId :{}",room.getRoomId());
        return room.getRoomId();
    }


    public MemberResponseDto getMember(String username){
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        ResponseEntity<MemberResponseDto> member = circuitBreaker.run(() ->
                        twinkleService.findByUsername(username),
                throwable -> ResponseEntity.ok(null));
        return member.getBody();
    }

}
