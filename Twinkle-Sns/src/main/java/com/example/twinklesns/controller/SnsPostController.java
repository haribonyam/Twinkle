package com.example.twinklesns.controller;

import com.example.twinklesns.dto.request.SnsPostRequestDto;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.example.twinklesns.service.SnsPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sns")
@RequiredArgsConstructor
public class SnsPostController {

    private final SnsPostService snsPostService;

    /***
     * sns 게시물 전체 조회
     * @param pageable
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<SnsPostResponseDto>> postList(
            @PageableDefault(size=12, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false,defaultValue = "") Map<String, String> searchCondition
    ){

        return ResponseEntity.ok(snsPostService.dynamicFindAll(pageable,searchCondition));
    }
    /**
     * sns 게시물 수정
     */
    @PutMapping("/posts")
    public ResponseEntity<HttpStatus> updatePost(@RequestBody SnsPostRequestDto snsPostRequestDto){
        snsPostService.updatePost(snsPostRequestDto);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * sns 게시물 작성
     */
    @PostMapping("/posts")
    public ResponseEntity<Long> savePost(@RequestPart("sns") SnsPostRequestDto snsPostRequestDto,
                                         @RequestPart(value="files",required = false) List<MultipartFile> files) throws IOException {

        return ResponseEntity.ok().body(snsPostService.savePost(snsPostRequestDto,files));
    }

    /**
     * sns 게시물 삭제
     * param : Long id
     */
    @DeleteMapping("/posts")
    public ResponseEntity<HttpStatus> deletePost(@RequestParam Long id){
        snsPostService.deletePost(id);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /**
     * sns 게시물 id로 단건 조회
     */
    @GetMapping("/posts/{id}")
    public ResponseEntity<SnsPostResponseDto> findPost(@PathVariable Long id){
        return ResponseEntity.ok(snsPostService.findById(id));
    }

    /**
     * 게시물 좋아요
     */
    @GetMapping("/posts/like/{id}")
    public ResponseEntity<HttpStatus> likePost(@PathVariable Long id,
                                               @RequestParam("condition") String condition){
        snsPostService.likePost(id,condition);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

    /***
     * sns 게시물 동적 검색
     * @return
     */
    @GetMapping("/posts/search")
    public ResponseEntity<Page<SnsPostResponseDto>> searchPost(
            @PageableDefault(size=8, sort="id", direction=Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false, defaultValue = "") Map<String,String> searchCondition
    ){

        return ResponseEntity.ok(snsPostService.dynamicFindAll(pageable,searchCondition));
    }



}
