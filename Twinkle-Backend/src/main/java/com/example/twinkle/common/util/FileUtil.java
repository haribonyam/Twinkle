package com.example.twinkle.common.util;

import com.example.twinkle.common.exception.ErrorCode;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class FileUtil {

    private static final String BASE_DIRECTORY =System.getProperty("user.dir")+"\\src\\main\\resources\\static\\file";

    /***
     * 파일 name 생성 (UUID)
     */
    public static String getCreateRandomName(){

            return UUID.randomUUID().toString();
    }

    /***
     * 파일 path 생성
     * @param fileName
     */
    public static String getFilePath(String fileName){

        return BASE_DIRECTORY+"/"+fileName;
    }

    public static String getRandFileName(MultipartFile file){
        String extension = StringUtils.getFilenameExtension(Objects.requireNonNull(file.getOriginalFilename()));
        if(!extension.equals("jpg")&&!extension.equals("png")){
            ErrorCode.throwExtensionNotAcceptable();
        }
        return getCreateRandomName()+"."+extension;
    }

}
