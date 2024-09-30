package com.example.twinklechat.controller;


import com.example.twinklechat.domain.entity.RoomEntity;
import com.example.twinklechat.dto.chat.Message;
import com.example.twinklechat.dto.request.ChatRequestDto;
import com.example.twinklechat.dto.response.RoomResponseDto;
import com.example.twinklechat.dto.response.StatusResponseDto;
import com.example.twinklechat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    /**
     * 채팅방 생성
     * @param requestDto
     * @param tradeBoardId
     * @param bindingResult
     * @return
     */
    @PostMapping("/room")
    public ResponseEntity<StatusResponseDto> createChatRoom(@RequestBody @Valid  final ChatRequestDto requestDto,
                                                         @RequestParam(required = false,name="id")Long tradeBoardId,
                                                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(StatusResponseDto.addStatus(400));
        }

        RoomEntity room  =chatService.createChatRoom(requestDto,tradeBoardId);
        return ResponseEntity.ok(StatusResponseDto.addStatus(200,room));
    }

    /**
     * 이용자 jwt 페이로드 정보로 채팅방 리스트 조회
     * @param accessToken
     * @return 200(success),400(fail)
     */
    @GetMapping("/room")
    public ResponseEntity<List<RoomResponseDto>> roomList(@RequestHeader("Authorization") String accessToken){
        log.info("CHAT JWT : {}",accessToken);
        List<RoomResponseDto> rooms = chatService.findRoomList(accessToken);
        if(rooms == null){
            log.info("data is null !!");
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(rooms);
    }

    /**
     * 채팅방 이미 존재하는지 체크
     * @param accessToken
     * @param tradeBoardId
     * @return
     */
    @GetMapping("/room/{id}")
    public ResponseEntity<Long> isExist(@RequestHeader("Authorization") String accessToken,
                                              @PathVariable("id") Long tradeBoardId){
        Long roomId =chatService.isExist(accessToken,tradeBoardId);
        return ResponseEntity.ok(roomId);
    }

    @MessageMapping("/{roomId}")
    @SendTo("/sub/{roomId}")
    public Message sendMessage(@DestinationVariable("roomId") Long roomId, @Valid Message message) {
        log.info("message controller");
        return message;
    }

    /**
     * 채팅 내용 저장 api
     * @param message
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<Message> saveChatting(@RequestBody Message message){
        log.info("chat save");
        chatService.saveChatting(message);
        return ResponseEntity.ok(message);

    }

    /**
     * 채팅방 입장시 사용자 인증 및 채팅 히스토리 return
     * @param roomId
     * @return
     */
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDto> enterRoom(@RequestHeader("Authorization") String accessToken,@PathVariable("roomId") Long roomId){
        RoomResponseDto roomResponseDto = chatService.enterRoom(accessToken,roomId);
        return ResponseEntity.ok(roomResponseDto);
    }



}
