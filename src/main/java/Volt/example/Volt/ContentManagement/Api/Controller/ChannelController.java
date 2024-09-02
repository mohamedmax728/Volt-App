package Volt.example.Volt.ContentManagement.Api.Controller;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelUpdateDto;
import Volt.example.Volt.ContentManagement.Application.Interfaces.ChannelService;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.CustomerManagement.Application.Services.JwtServiceImpl;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mobile/contentManagement/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    AuthService authService;
    @PostMapping("/create")
    public ResponseEntity<ServiceResponse> create(@Valid @RequestBody ChannelAddDto channelAddDto) {
        return ResponseEntity.ok(channelService.create(channelAddDto));
    }
    @PutMapping("/update")
    public ResponseEntity<ServiceResponse> update(@Valid @RequestBody ChannelUpdateDto channelUpdateDto) {
        return ResponseEntity.ok(channelService.update(channelUpdateDto, authService.getCurrentUserId()));
    }
    @GetMapping("/getMyChannel")
    public ResponseEntity<ServiceResponse> getMyChannel() {
        return ResponseEntity.ok(channelService.getCurrent(authService.getCurrentUserId()));
    }
    @GetMapping("/GetMany/{categoryId}")
    public ResponseEntity<ServiceResponse<PagedResult<ChannelSelectListDto>>>
    getManyByCategory(@PathVariable int categoryId, @RequestBody SearchModel searchModel) {
        return ResponseEntity.ok(channelService.getManyByCategory(categoryId,searchModel));
    }
}
