package Volt.example.Volt.Interaction.Application.Interfaces;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.Interaction.Application.Dtos.FollowerListDto;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

public interface FollowerService {
    ServiceResponse<PagedResult<ChannelSelectListDto>> getFolloweredChannels(SearchModel searchModel);
    ServiceResponse followAndUnfollow(FollowerListDto followerListDto);
}
