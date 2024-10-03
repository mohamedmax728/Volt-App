package Volt.example.Volt.Shared.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;
@RestController
@RequestMapping("/api/fileManager")
@RequiredArgsConstructor
public class FileManagerController {
    @Value("${uploadfilesDir}")
    private String uploadFilesDir;
//    @GetMapping("/getImage/{imageName}")
//    public Resource getImage(@PathVariable String imageName){
//        return UploadFiles.getFileAsResource(
//                uploadFilesDir + "/ProfilePicture/" , imageName);
//    }

    @GetMapping("/getImage/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        String imagePath = uploadFilesDir + "/ProfilePicture/"; // Path to the directory containing images

        try {
            // Create file path for the image
            Path filePath = Paths.get(imagePath + imageName);

            // Load image as a resource
            Resource resource = new UrlResource(filePath.toUri());

            // Determine the file's content type (e.g., image/jpeg, image/png)
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Default to binary if unknown
            }

            // Set headers for the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType)); // Set the correct content type (image/png, image/jpeg, etc.)

            // Return the image file as a response
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (IOException e) {
            // Handle file not found or other issues
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
