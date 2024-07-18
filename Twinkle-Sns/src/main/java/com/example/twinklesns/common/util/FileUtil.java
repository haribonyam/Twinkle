package com.example.twinklesns.common.util;


import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class FileUtil {
    private static final String BASE_DIRECTORY ="/file";

    /***
     * 파일 name 생성 (UUID)
     */
    public static String getCreateRandomName(){
        return UUID.randomUUID().toString();
    }

    /***
     * 파일 path 생성
     * @param file
     * @param fileName
     */
    public static String getFilePath(MultipartFile file, String fileName){

        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));
        return BASE_DIRECTORY+"/"+fileName+"."+extension;
    }
}
