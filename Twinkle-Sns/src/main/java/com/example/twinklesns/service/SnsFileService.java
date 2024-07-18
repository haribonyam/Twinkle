package com.example.twinklesns.service;

import com.example.twinklesns.common.exception.ErrorCode;
import com.example.twinklesns.common.util.FileUtil;
import com.example.twinklesns.domain.entity.FileEntity;
import com.example.twinklesns.domain.entity.SnsPostEntity;
import com.example.twinklesns.repository.SnsFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsFileService {

    private final SnsFileRepository snsFileRepository;

    @Transactional
    public List<FileEntity> uploadPostFiles(List<MultipartFile> files, SnsPostEntity snsPost) throws IOException {
        log.info("Upload image for post id :{}",snsPost.getId());
        List<FileEntity> fileEntities = new ArrayList<>();

        for(MultipartFile file : files){
            if(file.getSize()>1024*1024*3){
                ErrorCode.throwFileMaxSize();
            }
            FileEntity fileEntity = uploadFile(file,snsPost);
            snsPost.addFile(fileEntity);
            fileEntities.add(fileEntity);
        }
        snsFileRepository.saveAll(fileEntities);
        return fileEntities;
    }

    public FileEntity uploadFile(MultipartFile file , SnsPostEntity snsPost){
        String name = FileUtil.getCreateRandomName();
        String path = FileUtil.getFilePath(file,name);

        return FileEntity.builder()
                .name(name)
                .path(path)
                .snsPostEntity(snsPost)
                .build();
    }
}
