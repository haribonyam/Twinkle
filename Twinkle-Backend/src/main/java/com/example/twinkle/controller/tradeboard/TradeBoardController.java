package com.example.twinkle.controller.tradeboard;


import com.example.twinkle.dto.request.TradeBoardRequestDto;
import com.example.twinkle.dto.response.TradeBoardResponseDto;
import com.example.twinkle.service.tradeboard.TradeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;

    /**
     * 중고거래 게시판 글 작성
     * */
    @PostMapping("/tradeboard/save")
    public ResponseEntity<Long> TradeBoardPostSave(@RequestBody TradeBoardRequestDto tradeBoardRequestDto){
        Long id = tradeBoardService.saveTradeBoardPost(tradeBoardRequestDto);
        return ResponseEntity.ok().body(id);
    }

    @GetMapping("/tradeboard/{id}")
    public ResponseEntity<TradeBoardResponseDto> TradeBoardPost(@PathVariable Long id){
        TradeBoardResponseDto tradeBoardResponseDto = tradeBoardService.findById(id);
        return ResponseEntity.ok().body(tradeBoardResponseDto);
    }
}
