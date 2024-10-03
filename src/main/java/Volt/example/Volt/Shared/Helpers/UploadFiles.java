package Volt.example.Volt.Shared.Helpers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class UploadFiles {


    public static String uploadProfileImages(MultipartFile image, String path){

        if(Utilities.isNull(image)) return null;

        File uploadDir = new File(path);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String imgName = UUID.randomUUID().toString() + image.getOriginalFilename();
        String filePath = path + imgName;
        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imgName;
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

    public static Resource getFileAsResource(String path, String imageName){
        Path filePath = Paths.get(path + imageName);
        try {
            return new UrlResource(filePath.toUri());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // Get content type (MIME type) and check if it is an image
        String contentType = file.getContentType();

        if (contentType != null) {
            return contentType.startsWith("image/");
        }

        return false;
    }

    public static boolean hasImageExtension(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String filename = file.getOriginalFilename();

        if (filename != null) {
            String lowerCaseFilename = filename.toLowerCase();
            return lowerCaseFilename.endsWith(".jpg") ||
                    lowerCaseFilename.endsWith(".jpeg") ||
                    lowerCaseFilename.endsWith(".png") ||
                    lowerCaseFilename.endsWith(".gif") ||
                    lowerCaseFilename.endsWith(".bmp");
        }

        return false;
    }

}
