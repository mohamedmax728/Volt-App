package Volt.example.Volt.ContentManagement.Application.Interfaces;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.*;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

public interface ChannelService {
    ServiceResponse create(ChannelAddDto channelAddDto);
    ServiceResponse update(ChannelUpdateDto channelUpdateDto);
    ServiceResponse<ChannelDetailsDto> getCurrent();
    ServiceResponse<PagedResult<ChannelSelectListDto>> getManyByCategory(ChannelSearchModel searchModel);
    ServiceResponse<PagedResult<ChannelSelectListDto>> getTopMany(SearchModel searchModel);
}