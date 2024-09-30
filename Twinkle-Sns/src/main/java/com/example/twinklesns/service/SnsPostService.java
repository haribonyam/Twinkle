package com.example.twinklesns.service;

import com.example.twinklesns.common.exception.ErrorCode;
import com.example.twinklesns.domain.entity.CommentEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.domain.entity.like.PostLikeEntity;
import com.example.twinklesns.domain.entity.like.PostLikeId;
import com.example.twinklesns.dto.request.SnsPostRequestDto;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.example.twinklesns.repository.PostLikeRepository;
import com.example.twinklesns.repository.SnsPost.SnsPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsPostService {

    private final SnsPostRepository snsPostRepository;
    private final SnsFileService snsFileService;
    private final PostLikeRepository postLikeRepository;
    private final SnsCommentService snsCommentService;

    @Transactional
    public Long savePost(SnsPostRequestDto snsPostRequestDto, List<MultipartFile> files) throws IOException {
        SnsPostEntity snsPost = SnsPostEntity.builder()
                .title(snsPostRequestDto.getTitle())
                .memberId(snsPostRequestDto.getMemberId())
                .category(snsPostRequestDto.getCategory())
                .nickname(snsPostRequestDto.getNickname())
                .content(snsPostRequestDto.getContent())
                .viewCount(0)
                .likeCount(0)
                .build();
       Long id = snsPostRepository.save(snsPost).getId();
       if(files != null){
           snsFileService.uploadPostFiles(files,snsPost);
       }

       return id;
    }

    @Transactional
    public SnsPostResponseDto findById(Long postId,Long userId){
        Boolean isLiked = false;
        log.info("post id : {}",postId);
        //SnsPostEntity snsPost = snsPostRepository.findById(postId).orElseThrow(ErrorCode::throwSnsPostNotFound);
        SnsPostEntity snsPost = snsPostRepository.findPostById(postId).orElseThrow(ErrorCode::throwSnsPostNotFound);
        //List<CommentEntity> comments = snsCommentService.findByPostId(postId);
        if(userId != null) {
            PostLikeId postLikeId = PostLikeId.builder()
                    .postId(postId)
                    .userId(userId)
                    .build();

            isLiked = postLikeRepository.existsById(postLikeId);
        }
        snsPost.viewCountUp();

        return SnsPostResponseDto.builder()
                .snsPostEntity(snsPost)
                .isLiked(isLiked)
                .build();
    }

    @Transactional
    public void updatePost(SnsPostRequestDto snsPostRequestDto) {
        SnsPostEntity snsPost = snsPostRepository.findById(snsPostRequestDto.getId()).orElseThrow(ErrorCode::throwSnsPostNotFound);
        snsPost.updatePost(snsPostRequestDto);

    }

    @Transactional
    public void deletePost(Long id)
    {
        snsPostRepository.deleteById(id);
        postLikeRepository.deleteByPostId(id);
    }

    @Transactional
    public Page<SnsPostResponseDto> dynamicFindAll(Pageable pageable, Map<String, String> searchCondition) {

        return snsPostRepository.DynamicFindAll(pageable,searchCondition);
    }

    @Transactional
    public Long likePost(Long postId,SnsPostRequestDto snsPostRequestDto) {

        SnsPostEntity snsPost =snsPostRepository.findById(postId).orElseThrow(ErrorCode::throwSnsPostNotFound);

        PostLikeId postLikeId = PostLikeId.builder()
                .userId(snsPostRequestDto.getMemberId())
                .postId(postId)
                .build();

        // 이미 좋아요 했다면 좋아요 취소
        if (postLikeRepository.existsById(postLikeId)) {
            snsPost.goodCountDown();
            postLikeRepository.deleteById(postLikeId);
        }else {
            snsPost.goodCountUp();
            postLikeRepository.save(PostLikeEntity.builder()
                    .id(postLikeId)
                    .build());
        }

        return postId;
    }

}
