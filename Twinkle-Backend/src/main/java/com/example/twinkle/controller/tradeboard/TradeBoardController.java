package com.example.twinkle.controller.tradeboard;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TradeBoardController {

    /**
     * 중고거래 게시판 글 작성
     * */
    @PostMapping("/tradeboard/save")
    public ResponseEntity<?> TradeBoardSave(){

        return null;
    }
}
