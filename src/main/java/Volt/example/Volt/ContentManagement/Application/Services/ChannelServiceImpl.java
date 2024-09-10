package Volt.example.Volt.ContentManagement.Application.Services;

import Volt.example.Volt.ContentManagement.Application.Dtos.Category.CategoryDetailsDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelAddDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelDetailsDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelSelectListDto;
import Volt.example.Volt.ContentManagement.Application.Dtos.Channel.ChannelUpdateDto;
import Volt.example.Volt.ContentManagement.Application.Interfaces.ChannelService;
import Volt.example.Volt.ContentManagement.Application.Mapping.ChannelProfiler;
import Volt.example.Volt.ContentManagement.Domain.Entities.Category;
import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.ContentManagement.Domain.Repositories.ChannelRepository;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.CustomerManagement.Domain.Repositories.UserRepository;
import Volt.example.Volt.Shared.Dtos.PagedResult;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.Helpers.UploadFiles;
import Volt.example.Volt.Shared.Helpers.Utilities;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final AuthService authService;
    private final ChannelRepository channelRepository;
    private final ModelMapper modelMapper;
    private final ChannelProfiler channelProfiler;
    private final UserRepository userRepository;
    @Value("${uploadfilesDir}")
    private String uploadFilesDir;
    @Transactional
    @Override
    public ServiceResponse create(ChannelAddDto channelAddDto) {
        try {
            var currentUserId = authService.getCurrentUserId();
            Optional<Channel> currentChannel = channelRepository.findChannelByUser_Id(currentUserId);
            if(currentChannel.isPresent()){
                return new ServiceResponse<>(null, false,
                        "there is channel created before!!", "هناك قناة تم انشاءها مسبقا!!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(channelRepository.existsByNameIgnoreCase(channelAddDto.getName())){
                return new ServiceResponse<>(null, false,
                        "this name is selected before!!", "هذا الاسم تم اختياره مسبقا!!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            var entity = channelProfiler.fromChannelAddDto(channelAddDto);
            var categories = channelAddDto.getCategories().stream().map(
                    id->{
                        var item = new Category();
                        item.setId(id);
                        return item;
                    }
            ).collect(Collectors.toSet());
            entity.setCategories(categories);
            UUID currentUser = authService.getCurrentUserId();
            User user = (User)userRepository.findById(currentUser).get();
            entity.setUser(user);
            user.setFullName(channelAddDto.getName());

            if(!Utilities.isNullOrEmpty(user.getImagePath())){
                UploadFiles.deleteFile(user.getImagePath());
            }
            String profileImgPath = UploadFiles.uploadProfileImages(channelAddDto.getProfileImage(),
                    uploadFilesDir + "/ProfilePicture/");
            String backGroundImgPath = UploadFiles.uploadProfileImages(channelAddDto.getBackgoundImage(),
                    uploadFilesDir + "/ProfilePicture/");
            entity.setImagePath(profileImgPath);
            entity.setBackgoundImagePath(backGroundImgPath);
            user.setImagePath(profileImgPath);

            channelRepository.save(entity);
            return new ServiceResponse<>(null, true,
                    "Channel created successfully!!", "تم انشاء القناة بنجاح!!",
                    HttpStatus.CREATED);
        }
        catch (Exception ex) {
            return new ServiceResponse<>(null, false,
                    "Internal Error", "حدث خطأ داخلي!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @Override
//    @CacheEvict(value = "channelDetails", key = "#userId")
    public ServiceResponse update(ChannelUpdateDto channelUpdateDto) {
        try {
            UUID currentUserId = authService.getCurrentUserId();
            Optional<Channel> currentChannel = channelRepository.findChannelByUser_Id(currentUserId);
            if(currentChannel.isEmpty()){
                return new ServiceResponse<>(null, false,
                        "there is no channel created before related to this account!!",
                        "لا يوجد قناة مسبقة لها صلة هذا الحساب!!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Optional<Channel> isChannelNameExistsBefore =
                    channelRepository.findByNameIgnoreCase(channelUpdateDto.getName());
            if(isChannelNameExistsBefore.isPresent() && currentChannel.get().getId() !=
                    isChannelNameExistsBefore.get().getId()){
                return new ServiceResponse<>(null, false,
                        "this name is selected before!!", "هذا الاسم تم اختياره مسبقا!!",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Channel channel = currentChannel.get();
            channel.setCategories(channel.getCategories().stream().filter(
                    category -> !channelUpdateDto.getCategories().contains(category.getId())
            ).collect(Collectors.toSet()));
            modelMapper.map(channelUpdateDto, channel);
            var addedCategories = channelUpdateDto.getCategories().stream().map(
                    id->{
                        var item = new Category();
                        item.setId(id);
                        return item;
                    }
            ).collect(Collectors.toSet());
            channel.getCategories().addAll(addedCategories);

            if(!Utilities.isNullOrEmpty(channel.getUser().getImagePath()) && channelUpdateDto.getIsProfileImageUpdated() ){
                UploadFiles.deleteFile(channel.getUser().getImagePath());
            }
            if(channelUpdateDto.getIsBackgoundImageUpdated()){
                UploadFiles.deleteFile(channel.getBackgoundImagePath());
            }

            String profileImgPath = UploadFiles.uploadProfileImages(channelUpdateDto.getProfileImage()
                ,uploadFilesDir + "/ProfilePicture/");
            String backGroundImgPath = UploadFiles.uploadProfileImages(channelUpdateDto.getBackgoundImage()
                ,uploadFilesDir + "/ProfilePicture/");
            channel.setImagePath(profileImgPath);
            channel.setBackgoundImagePath(backGroundImgPath);
            channel.getUser().setImagePath(profileImgPath);
            channel.getUser().setFullName(channelUpdateDto.getName());
            channelRepository.save(channel);
            return new ServiceResponse<>(null, true,
                    "Channel updated successfully!!", "تم تعديل القناة بنجاح!!",
                    HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ServiceResponse<>(null, false,
                    "Internal Error", "حدث خطأ داخلي!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    @Override
//    @Cacheable(key = "#userId", value = "channelDetails")
    public ServiceResponse<ChannelDetailsDto> getCurrent() {
        UUID currentUserId = authService.getCurrentUserId();
        Optional<Channel> channelOpt = channelRepository.findChannelByUser_Id(currentUserId);
        if(channelOpt.isEmpty()){
            return new ServiceResponse<>(null, false,"Channel is not exists",
                    "هذه القناة غير موجودة", HttpStatus.NOT_FOUND);
        }
        Channel channel = channelOpt.get();
        var channelDetailsDto = modelMapper.map(channelOpt.get(), ChannelDetailsDto.class);
        channelDetailsDto.setCategoryDetailsDto(new HashSet<>());
         channel.getCategories()
                 .forEach(category -> {
                     channelDetailsDto.getCategoryDetailsDto().add(modelMapper.map(
                             category, CategoryDetailsDto.class
                     ));
                 });
        channelDetailsDto.setNumOfFollowing(channel.getUser().getNumOfFollowing());
        channelDetailsDto.setEmail(channel.getUser().getEmail());
        channelDetailsDto.setProfileImage(UploadFiles.downloadFile(channel.getImagePath()));
        channelDetailsDto.setBackgoundImage(UploadFiles.downloadFile(channel.getBackgoundImagePath()));
        return new ServiceResponse<>(channelDetailsDto,
                true,"","", HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @Override
    public ServiceResponse<PagedResult<ChannelSelectListDto>> getManyByCategory(int categoryId, SearchModel searchModel) {

        Pageable page = Utilities.makePagable(
                searchModel
        );
        Page<Channel> channelPages = channelRepository.findAllByCategoryIdAndNameContainingIgnoreCase(categoryId,
                searchModel.getName(), page);

        if(channelPages.isEmpty()){
            return new ServiceResponse<>(null, false,"there is no channels",
                    "لا يوجد قنوات", HttpStatus.NOT_FOUND);
        }

        return new ServiceResponse<PagedResult<ChannelSelectListDto>>
                (channelProfiler.toChannelSelectListDto(channelPages),
                        true, "","", HttpStatus.OK);
    }
}
