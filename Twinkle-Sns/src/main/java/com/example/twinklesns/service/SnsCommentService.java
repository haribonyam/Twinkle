package com.example.twinklesns.service;

import com.example.twinklesns.common.exception.ErrorCode;
import com.example.twinklesns.domain.entity.CommentEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.dto.request.SnsCommentRequestDto;
import com.example.twinklesns.dto.request.SnsPostRequestDto;
import com.example.twinklesns.repository.SnsCommentRepository;
import com.example.twinklesns.repository.SnsPost.SnsPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsCommentService {

    private final SnsCommentRepository snsCommentRepository;
    private final SnsPostRepository snsPostRepository;

    @Transactional
    public  Long saveComment(Long id, SnsPostRequestDto snsPostRequestDto) {

        SnsPostEntity snsPost = snsPostRepository.findById(id).orElseThrow(ErrorCode::throwSnsPostNotFound);
        CommentEntity comment =  CommentEntity.builder()
                .postId(id)
                .content(snsPostRequestDto.getContent())
                .nickname(snsPostRequestDto.getNickname())
                .build();
        //snsPost.addComment(comment);

        snsCommentRepository.save(comment);

        return id;
    }
    @Transactional
    public Long saveReply(Long id, SnsCommentRequestDto snsCommentRequestDto) {
        SnsPostEntity snsPost = snsPostRepository.findById(id).orElseThrow(ErrorCode::throwSnsPostNotFound);
        CommentEntity parent = snsCommentRepository.findById(snsCommentRequestDto.getParentId()).orElseThrow(ErrorCode::throwSnsCommentNotFound);

        CommentEntity child = CommentEntity.builder()
                .content(snsCommentRequestDto.getContent())
                .nickname(snsCommentRequestDto.getNickname())
                .snsPost(snsPost)
                .build();

        parent.addChild(child);

        snsCommentRepository.save(child);

        return id;
    }

    @Transactional
    public Long updateComment(Long id, SnsCommentRequestDto snsCommentRequestDto) {

        String modify = snsCommentRequestDto.getIsDelete()?"삭제된 댓글 입니다.":snsCommentRequestDto.getContent();
        CommentEntity comment = snsCommentRepository.findById(id).orElseThrow(ErrorCode::throwSnsCommentNotFound);
        comment.updateContent(modify);

        return comment.getPostId();
    }

    public List<CommentEntity> findByPostId(Long postId) {
        return snsCommentRepository.findCommentsByPostId(postId);
    }
}
