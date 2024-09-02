package Volt.example.Volt.ContentManagement.Application.Interfaces;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelDetailsDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelUpdateDto;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ServiceResponse create(ChannelAddDto channelAddDto);
    ServiceResponse update(ChannelUpdateDto channelUpdateDto, UUID userId);
    ServiceResponse<ChannelDetailsDto> getCurrent(UUID userId);
    ServiceResponse<PagedResult<ChannelSelectListDto>> getManyByCategory(int categoryId, SearchModel searchModel);
}