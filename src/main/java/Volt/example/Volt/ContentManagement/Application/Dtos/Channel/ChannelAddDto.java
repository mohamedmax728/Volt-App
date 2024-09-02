package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChannelAddDto
 {
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
         @NotNull(message = "Categories must be not null")
         @Size(min = 1, message = "Must select at least one category")
         Set<Integer> categories;
}
