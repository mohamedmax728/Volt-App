package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ChannelListDto {
    private int id;
    private String name;
    private Long numOfFollowers;
}
