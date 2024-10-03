package Volt.example.Volt.ContentManagement.Api.Controller;

import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSearchModel;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelUpdateDto;
import Volt.example.Volt.ContentManagement.Application.Interfaces.ChannelService;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contentManagement/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @PostMapping(value = "/create", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ServiceResponse> create(@Valid @ModelAttribute ChannelAddDto channelAddDto) {
        return ResponseEntity.ok(channelService.create(channelAddDto));
    }
    @PutMapping(value = "/update", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ServiceResponse> update(@Valid @ModelAttribute ChannelUpdateDto channelUpdateDto) {
        return ResponseEntity.ok(channelService.update(channelUpdateDto));
    }
    @GetMapping("/getMyChannel")
    public ResponseEntity<ServiceResponse> getMyChannel() {
        return ResponseEntity.ok(channelService.getCurrent());
    }
    @GetMapping("/getManyByCategory")
    public ResponseEntity<ServiceResponse<PagedResult<ChannelSelectListDto>>>
    getManyByCategory(@RequestBody ChannelSearchModel searchModel) {
        return ResponseEntity.ok(channelService.getManyByCategory(searchModel));
    }
    @GetMapping("/getTopChannels")
    public ResponseEntity<ServiceResponse<PagedResult<ChannelSelectListDto>>>
    getTopChannels(@RequestBody ChannelSearchModel searchModel) {
        return ResponseEntity.ok(channelService.getTopMany(searchModel));
    }
}
