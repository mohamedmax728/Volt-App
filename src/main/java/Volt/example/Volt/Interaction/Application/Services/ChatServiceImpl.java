package Volt.example.Volt.Interaction.Application.Services;

import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.Interaction.Application.Dtos.ChatRoomListDto;
import Volt.example.Volt.Interaction.Application.Interfaces.ChatService;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatRoom;
import Volt.example.Volt.Interaction.Domain.Enums.MessageStatus;
import Volt.example.Volt.Interaction.Domain.Enums.MessageType;
import Volt.example.Volt.Interaction.Domain.Repositories.ChatMessageRepository;
import Volt.example.Volt.Interaction.Domain.Repositories.ChatRoomRepository;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.Helpers.Utilities;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;
import java.util.UUID;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final AuthService authService;

    @Transactional
    public ServiceResponse getOrCreateChatRoom(UUID userId){
        try {
            chatRoomRepository.save(new ChatRoom(userId, authService.getCurrentUserId()));
            return new ServiceResponse
                    ("",
                            true, "","", HttpStatus.OK);
        }
        catch (Exception e){
            return new ServiceResponse("",false, "","", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ServiceResponse<ChatMessage> sendMessage(ChatMessage chatMessage) {
        try{
            chatMessage.setStatus(MessageStatus.SENT);
            chatMessageRepository.save(chatMessage);

            messagingTemplate.convertAndSendToUser(
                    chatMessage.getChatRoomId().toString() + '/'+
                            chatMessage.getChatRoom().getRecipientId().toString()
                    ,
                    "/queue/messages", chatMessage);
            return new ServiceResponse<>(chatMessage, true, "","", HttpStatus.OK);
        }catch (Exception e){
            return new ServiceResponse<>(null, false, "","", HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public ServiceResponse updateMessageStatus(Long messageId,Long chatRoomId, MessageStatus status) {
        try{
            ChatMessage message = chatMessageRepository.findById(messageId).orElseThrow();
            message.setStatus(status);
            chatMessageRepository.save(message);
            messagingTemplate.convertAndSendToUser(
//                String.format("%s/%s",message.getSenderId(),message.getRecipientId())
                    chatRoomId.toString() + "/" +message.getSenderId().toString()
                    , "/queue/status", message);
            return new ServiceResponse(null, true, "","", HttpStatus.OK);
        }
        catch(Exception e){
            return new ServiceResponse(null, false, "","", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ServiceResponse sendTypingNotification(UUID sender, UUID receiver, Long chatRoomId, boolean isTyping) {
        try {
            ChatMessage typingMessage = new ChatMessage();
            typingMessage.setSenderId(sender);
            typingMessage.setChatRoomId(chatRoomId);
            typingMessage.setType(isTyping ? MessageType.TYPING : MessageType.STOP_TYPING);

            messagingTemplate.convertAndSendToUser(
                    chatRoomId.toString() + "/" + receiver
                    , "/queue/typing", typingMessage);
            return new ServiceResponse(null, true
                    , "","", HttpStatus.OK);

        }catch (Exception e){
            return new ServiceResponse(null, false, "","", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public ServiceResponse<List<ChatMessage>> getAllMessagesInSpecficRoom(Long roomId, SearchModel searchModel){
        try {
            Pageable page = Utilities.makePagable(
                    searchModel
            );
            var result = chatMessageRepository.findByChatRoomId(roomId, page);
            return new ServiceResponse<>(result
            , true,"","", result.isEmpty() ? HttpStatus.NOT_FOUND:HttpStatus.OK);
        }catch (Exception e){
            return new ServiceResponse<>(null
                    , false,"","", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public ServiceResponse<List<ChatRoomListDto>> getAllChatRoomForCurrentUser(SearchModel searchModel){
        try {
            Pageable page = Utilities.makePagable(
                    searchModel
            );
            var result = chatMessageRepository.findBySenderIdAndRecipientIdOrderByCreatedDate(
                    authService.getCurrentUserId(),searchModel.getName().trim() , page);
            return new ServiceResponse<>(result
                    , true,"","", result.isEmpty() ? HttpStatus.NOT_FOUND:HttpStatus.OK);
        }catch (Exception e){
            return new ServiceResponse<>(null
                    , false,"","", HttpStatus.BAD_REQUEST);
        }
    }
}
