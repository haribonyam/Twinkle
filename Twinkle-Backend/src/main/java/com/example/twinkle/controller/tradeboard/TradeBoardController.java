package com.example.twinkle.controller.tradeboard;


import com.example.twinkle.dto.request.TradeBoardRequestDto;
import com.example.twinkle.dto.response.TradeBoardResponseDto;
import com.example.twinkle.service.tradeboard.TradeBoardService;
import jakarta.servlet.ServletOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;

    /**
     * 중고거래 게시판 글 작성
     * */
    @PostMapping("/tradeboard/save")
    public ResponseEntity<Long> TradeBoardPostSave(@RequestPart TradeBoardRequestDto tradeBoardRequestDto,@RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        Long id = tradeBoardService.saveTradeBoardPost(tradeBoardRequestDto,files);
        return ResponseEntity.ok().body(id);
    }

    /**
     * 중고거래 게시물 id로 단건 삭제
     * @param id
     */
    @DeleteMapping("/tradeboard/delete/{id}")
    public ResponseEntity<? > deletePost(@PathVariable Long id){
        tradeBoardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /***
     * 중고거래 게시글 id로 게시글 수정 (제목, 내용, 가격)
     * @param id
     * @param tradeBoardRequestDto
     */
    @PutMapping("/tradeboard/update/{id}")
    public ResponseEntity<? > updatePost(@PathVariable Long id, @RequestBody TradeBoardRequestDto tradeBoardRequestDto){

        tradeBoardService.updatePost(id, tradeBoardRequestDto);
        return ResponseEntity.ok().build();
    }

    /***
     * 게시물 전체 조회 , 검색 필터별 전체 게시글 동적 조회
     * @param pageable
     */
    @GetMapping("/tradeboard/list")
    public ResponseEntity<Page<TradeBoardResponseDto>> TradeBoardAll(
            @PageableDefault(size=12, sort="id", direction=Sort.Direction.DESC)Pageable pageable,@RequestBody Map<String,String> searchCondition){

        Page<TradeBoardResponseDto> tradeBoardResponseDtos = tradeBoardService.tradeBoardAllList(pageable,searchCondition);

        return ResponseEntity.ok(tradeBoardResponseDtos);
    }

    /***
     * 중고거래 게시글 세부 정보
     * @param id
     */
    @GetMapping("/tradeboard/{id}")
    public ResponseEntity<TradeBoardResponseDto> TradeBoardPost(@PathVariable Long id){
        return ResponseEntity.ok(tradeBoardService.findById(id));
    }

}
