package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
public class ChannelListDto {
    private int id;
    private String name;
    private MultipartFile profileImage;
    private Long numOfFollowers;
}
