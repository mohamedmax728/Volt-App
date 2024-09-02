package Volt.example.Volt.Interaction.Api.Controller;

import Volt.example.Volt.Interaction.Application.Interfaces.ChatService;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import Volt.example.Volt.Interaction.Domain.Enums.MessageStatus;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(ChatMessage message) {
        chatService.sendMessage(message);
    }
    @MessageMapping("/chat.typing")
    public void typing(UUID sender, UUID receiver, Long chatRoomId, boolean isTyping) {
        chatService.sendTypingNotification( sender, receiver, chatRoomId, isTyping);
    }
    @MessageMapping("/chat.messageStatus")
    public void updateMessageStatus(Long messageId,Long chatRoomId, MessageStatus status) {
        chatService.updateMessageStatus(messageId, chatRoomId, status);
    }
    @GetMapping("/getAllMessagesInSpecficRoom")
    public void getAllMessagesInSpecficRoom(Long roomId, SearchModel searchModel) {
        chatService.getAllMessagesInSpecficRoom(roomId, searchModel);
    }
    @GetMapping("/getAllChatRoomForCurrentUser")
    public void getAllChatRoomForCurrentUser(SearchModel searchModel) {
        chatService.getAllChatRoomForCurrentUser(searchModel);
    }
}
