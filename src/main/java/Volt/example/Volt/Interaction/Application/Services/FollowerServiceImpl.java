package Volt.example.Volt.Interaction.Application.Services;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Mapping.ChannelProfiler;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.Interaction.Application.Dtos.FollowerListDto;
import Volt.example.Volt.Interaction.Application.Interfaces.FollowerService;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.Follower;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.FollowerId;
import Volt.example.Volt.Interaction.Domain.Repositories.FollowerRepository;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.Helpers.Utilities;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Collectors;


@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final FollowerRepository followerRepository;
    private final AuthService authService;
    private final ChannelProfiler channelProfiler;
    @Transactional(readOnly = true)
    public ServiceResponse<PagedResult<ChannelSelectListDto>> getFolloweredChannels(SearchModel searchModel){

        Pageable page = Utilities.makePagable(
                searchModel
        );

        var currentUserId = authService.getCurrentUserId();
        Page<Follower> followerPages = followerRepository.findFollowersByChannelNameAndUserId(currentUserId, searchModel.getName(), page);
        if(followerPages.isEmpty()){
            return new ServiceResponse<>(null, false,"there is no channels",
                    "لا يوجد قنوات", HttpStatus.NOT_FOUND);
        }

        return new ServiceResponse<PagedResult<ChannelSelectListDto>>
                (channelProfiler.toChannelSelectListDto(followerPages.map(s->s.getChannel())),
                        true, "","", HttpStatus.OK);
    }

    @Transactional
    public ServiceResponse followAndUnfollow(FollowerListDto followerListDto){
        try{
            var currentUserId = authService.getCurrentUserId();
            var followerslist = followerListDto.getFollowers().stream().map(
                    s-> new Follower(new FollowerId(s, currentUserId), null,null)
            ).collect(Collectors.toSet());
            followerRepository.AddAndDeleteByUserIdAndChannelIdInAndUpdateNumOfFollowingAndFollowers
                    (currentUserId,
                            followerListDto.getFollowers().stream().mapToInt(Integer::intValue).toArray(),
                            followerListDto.getUnFollowers().stream().mapToInt(Integer::intValue).toArray()
                            );
            return new ServiceResponse(null,true,  "", "", HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ServiceResponse(null, false,
                    "Internal Error", "حدث خطأ داخلي!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
