package com.example.twinkle.service.tradeboard;

import com.example.twinkle.common.exception.CustomException;
import com.example.twinkle.common.exception.ErrorCode;
import com.example.twinkle.common.util.FileUtil;
import com.example.twinkle.domain.entity.FileEntity;
import com.example.twinkle.domain.entity.TradeBoardEntity;
import com.example.twinkle.repository.FileRepository;
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
public class FileService {
    private final FileRepository fileRepository;

    @Transactional
    public List<FileEntity> saveFile(List<MultipartFile> files, TradeBoardEntity tradeBoardEntity) {

        List<FileEntity> fileEntities = new ArrayList<>();

        for(MultipartFile file : files){
            if(file.getSize()>1024*1024*3){
                throw ErrorCode.throwFileMaxSize();
            }
            FileEntity image = setFile(file,tradeBoardEntity);
            tradeBoardEntity.getFiles().add(image);
            fileEntities.add(image);
        }
        fileRepository.saveAll(fileEntities);

        return fileEntities;
    }

    public FileEntity setFile(MultipartFile file, TradeBoardEntity tradeBoard){
        String fileName = FileUtil.getRandFileName(file);
        String path = FileUtil.getFilePath(fileName);
        File saveFile = new File(path);
       try {
           file.transferTo(saveFile);
       }catch(IOException e){
           log.info("file Exception ");
       }
        return FileEntity.builder()
                .name(fileName)
                .path("/file/"+fileName)
                .tradeBoardEntity(tradeBoard)
                .build();
    }
}
