package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelSelectListDto {
    private int id;
    private String name;
    private Long numOfFollowers;
    private byte[] profileImage;

}
