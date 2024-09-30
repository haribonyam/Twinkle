package com.example.twinklesns.controller;

import com.example.twinklesns.dto.request.SnsCommentRequestDto;
import com.example.twinklesns.dto.request.SnsPostRequestDto;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.example.twinklesns.service.SnsCommentService;
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
@RequiredArgsConstructor
public class SnsPostController {

    private final SnsPostService snsPostService;
    private final SnsCommentService snsCommentService;

    /***
     * sns 게시물 전체 조회
     * @param pageable
     * @return
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<SnsPostResponseDto>> postList(
            @PageableDefault(size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false,defaultValue = "") Map<String, String> searchCondition
    ){
        System.out.println(searchCondition.get("category"));
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
    public ResponseEntity<Long> savePost(@RequestPart("SnsPostRequestDto") SnsPostRequestDto snsPostRequestDto,
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
    public ResponseEntity<SnsPostResponseDto> findPost(@PathVariable("id") Long id,@RequestParam(value = "memberId", required = false) Long memberId){
        return ResponseEntity.ok(snsPostService.findById(id,memberId));
    }


    /***
     * sns 게시물 동적 검색
     */
    @GetMapping("/posts/search")
    public ResponseEntity<Page<SnsPostResponseDto>> searchPost(
            @PageableDefault(size=8, sort="id", direction=Sort.Direction.DESC)Pageable pageable,
            @RequestParam(required = false, defaultValue = "") Map<String,String> searchCondition
    ) {

        return ResponseEntity.ok(snsPostService.dynamicFindAll(pageable, searchCondition));
    }

    /**
     * sns 댓글 달기
     * @param id
     * @param snsPostRequestDto
     * @return
     */
    @PostMapping("/comments/{id}")
    public ResponseEntity<Long> saveComment(
            @PathVariable Long id, @RequestBody SnsPostRequestDto snsPostRequestDto
    ){

        return ResponseEntity.ok(snsCommentService.saveComment(id,snsPostRequestDto));
    }
    /**
     * sns 대댓글 달기
     */
    @PostMapping("/comments/reply/{id}")
    public ResponseEntity<Long> saveReply(
            @PathVariable Long id , @RequestBody SnsCommentRequestDto snsCommentRequestDto
            ){
        return ResponseEntity.ok(snsCommentService.saveReply(id,snsCommentRequestDto));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Long> updateComment(
            @PathVariable Long id, @RequestBody SnsCommentRequestDto snsCommentRequestDto
    ){

        return ResponseEntity.ok(snsCommentService.updateComment(id,snsCommentRequestDto));
    }

    /***
     * SNS 게시글 좋아요
     */
    @PostMapping("/like/{postId}")
    public ResponseEntity<Long> likePost(@PathVariable Long postId,@RequestBody SnsPostRequestDto snsPostRequestDto){
        return ResponseEntity.ok(snsPostService.likePost(postId,snsPostRequestDto));
    }
    /***
     * SNS 게시글 좋아요 취소
//     */
//    @PostMapping("/like/{postId}")
//    public ResponseEntity<Long> dislikePost(@PathVariable Long postId, @RequestBody SnsPostRequestDto snsPostRequestDto){
//        return ResponseEntity.ok(snsPostService.dislikePost());
//    }

}
