package Volt.example.Volt.ContentManagement.Application.Interfaces;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelDetailsDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelUpdateDto;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

public interface ChannelService {
    ServiceResponse create(ChannelAddDto channelAddDto);
    ServiceResponse update(ChannelUpdateDto channelUpdateDto);
    ServiceResponse<ChannelDetailsDto> getCurrent();
    ServiceResponse<PagedResult<ChannelSelectListDto>> getManyByCategory(int categoryId, SearchModel searchModel);
}