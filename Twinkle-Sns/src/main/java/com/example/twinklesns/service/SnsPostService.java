package com.example.twinklesns.service;

import com.example.twinklesns.common.exception.ErrorCode;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.dto.request.SnsPostRequestDto;
import com.example.twinklesns.dto.response.SnsPostResponseDto;
import com.example.twinklesns.repository.SnsPost.SnsPostRepository;
import lombok.RequiredArgsConstructor;
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
public class SnsPostService {

    private final SnsPostRepository snsPostRepository;
    private final SnsFileService snsFileService;

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
    public SnsPostResponseDto findById(Long id) {
        SnsPostEntity snsPost = snsPostRepository.findPostById(id).orElseThrow(ErrorCode::throwSnsPostNotFound);
        snsPost.viewCountUp();
        return SnsPostResponseDto.builder()
                .snsPostEntity(snsPost)
                .build();
    }

    @Transactional
    public void updatePost(SnsPostRequestDto snsPostRequestDto) {
        SnsPostEntity snsPost = snsPostRepository.findById(snsPostRequestDto.getId()).orElseThrow(ErrorCode::throwSnsPostNotFound);
        snsPost.updatePost(snsPostRequestDto);

    }

    @Transactional
    public void deletePost(Long id) {
        snsPostRepository.deleteById(id);
    }

    @Transactional
    public Page<SnsPostResponseDto> dynamicFindAll(Pageable pageable, Map<String, String> searchCondition) {

        return snsPostRepository.DynamicFindAll(pageable,searchCondition);
    }

    @Transactional
    public void likePost(Long id,String condition) {
        SnsPostEntity snsPost =snsPostRepository.findById(id).orElseThrow(ErrorCode::throwSnsPostNotFound);
        if(condition.equals("up")){
            snsPost.goodCountUp();
        }
        if(condition.equals("down")){
            snsPost.goodCountDown();
        }

    }
}
