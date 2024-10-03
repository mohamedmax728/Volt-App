package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;

import Volt.example.Volt.Shared.Helpers.UploadFiles;
import Volt.example.Volt.Shared.Helpers.Utilities;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
public class ChannelUpdateDto {
    @NotEmpty(message = "name must be not empty")
    @NotBlank(message = "name must be not blank")
    @NotNull(message = "name must be not null")
    String name;
    @NotEmpty(message = "bio must be not empty")
    @NotBlank(message = "bio must be not blank")
    @NotNull(message = "bio must be not null")
    String bio;
    String discordUrl;
    String xUrl;
    String instagramUrl;
    String youtubeUrl;
    String facebookUrl;
    MultipartFile backgoundImage;
    MultipartFile profileImage;
    Boolean isProfileImageUpdated = false;
    Boolean isBackgoundImageUpdated = false;
    @NotNull(message = "Categories must be not null")
    @Size(min = 1, message = "Must select at least one category")
    Set<Integer> categories;

    @AssertTrue(message = "please, choose image for your profile image")
    boolean isProfileImage(){return !Utilities.isNull(profileImage) && UploadFiles.isImageFile(profileImage);}

    @AssertTrue(message = "please, choose image for your profile image")
    boolean isBackgroundImage(){return !Utilities.isNull(backgoundImage) && UploadFiles.isImageFile(backgoundImage);}
}