package Volt.example.Volt.ContentManagement.Application.Mapping;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Component
public class ChannelProfiler {
    private final ModelMapper modelMapper;
    public Channel fromChannelAddDto(ChannelAddDto channelAddDto){

        var entity = modelMapper.map(channelAddDto, Channel.class);
        entity.setNumOfFollowers(0L);
        return entity;
    }
    public PagedResult<ChannelSelectListDto> toChannelSelectListDto(Page<Channel> channels){
        var pagedResult = new PagedResult<ChannelSelectListDto>();
        pagedResult.setData(
                channels.stream().map(
                        item -> modelMapper.map(item, ChannelSelectListDto.class)
                ).collect(Collectors.toList())
        );
        pagedResult.setPageSize(channels.getSize());
        pagedResult.setPageNo(channels.getNumber() + 1);
        pagedResult.setTotalElements(channels.getTotalElements());
        pagedResult.setTotalPages(channels.getTotalPages());
        pagedResult.setLast(channels.isLast());
        return pagedResult;
    }

}
