package Volt.example.Volt.Shared.Helpers;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFiles {


    public static String uploadProfileImages(MultipartFile image, String path){

        if(Utilities.isNull(image)) return null;

        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = path + image.getOriginalFilename();
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    public static Boolean deleteFile(String name){
        Path filePath = Paths.get(name);

        boolean isDeleted = false;
        try {
            isDeleted = Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return isDeleted;
    }

    public static byte[] downloadFile(String imageName){
        Path filePath = Paths.get(imageName);
        try {
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
