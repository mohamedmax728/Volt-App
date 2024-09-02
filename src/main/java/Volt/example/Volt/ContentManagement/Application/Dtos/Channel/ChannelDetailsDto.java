package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;


import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryDetailsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChannelDetailsDto {
    private String name;
    private String bio;
    private String discordUrl;
    private String xUrl;
    private String instagramUrl;
    private String youtubeUrl;
    private String facebookUrl;
    private Long numOfFollowers;
    private Long numOfFollowing;
    private String email;

    private Set<CategoryDetailsDto> categoryDetailsDto;
}
