package com.sadang.storybada.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

//    public static final String FILE_UPLOAD_PATH = "D:\\기타 사소한 프로그램들\\StoryBada Project\\upload";
//    public static final String FILE_UPLOAD_PATH = "D:\\SaDang\\FinalProject\\Upload";
    public static final String FILE_UPLOAD_PATH = "/home/ec2-user/upload/images";

    public static String saveFile(long userId, MultipartFile multipartFile) {

        if (multipartFile == null) {
            return null;
        }

        String directoryName = "/" + userId + "_" + System.currentTimeMillis() + "/";
        String directoryPath = FILE_UPLOAD_PATH + directoryName;
        File directory = new File(directoryPath);

        if(!directory.mkdir()) {
            return null;
        }

        String filePath = directoryPath + multipartFile.getOriginalFilename();

        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, bytes);
        } catch (IOException e) {
            return null;
        }

        return "/images" + directoryName + multipartFile.getOriginalFilename();
    }

    public static boolean deleteFile(String filePath) {

        if(filePath == null) {
            return true;
        }

        String fullFilePath = FILE_UPLOAD_PATH + filePath.replace("/images", "");
        Path path = Paths.get(fullFilePath);
        Path dirPath = path.getParent();

        try {
            Files.delete(path);
            Files.delete(dirPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
