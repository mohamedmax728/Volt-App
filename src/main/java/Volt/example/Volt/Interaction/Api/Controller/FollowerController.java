package Volt.example.Volt.Interaction.Api.Controller;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.Interaction.Application.Dtos.FollowerListDto;
import Volt.example.Volt.Interaction.Application.Interfaces.FollowerService;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile/interaction/follower")
@RequiredArgsConstructor
public class FollowerController {
    private final FollowerService followerService;

    @GetMapping("/getFolloweredChannels")
    public ResponseEntity<ServiceResponse<PagedResult<ChannelSelectListDto>>> getFolloweredChannels(
          @RequestBody SearchModel searchModel
    ){
        return ResponseEntity.ok(followerService.getFolloweredChannels(searchModel));
    }
    @PutMapping("/followAndUnfollow")
    public ResponseEntity<ServiceResponse> followAndUnfollow(@RequestBody FollowerListDto followerListDto){
        return ResponseEntity.ok(followerService.followAndUnfollow(followerListDto));
    }
}
