package Volt.example.Volt.ContentManagement.Application.Dtos.Channel;

import Volt.example.Volt.Shared.Dtos.SearchModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class ChannelSearchModel extends SearchModel {
    private Set<Integer> categories;
}
