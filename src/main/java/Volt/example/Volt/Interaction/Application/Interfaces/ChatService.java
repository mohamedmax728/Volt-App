package Volt.example.Volt.Interaction.Application.Interfaces;

import Volt.example.Volt.Interaction.Application.Dtos.ChatRoomListDto;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import Volt.example.Volt.Interaction.Domain.Enums.MessageStatus;
import Volt.example.Volt.Shared.Dtos.SearchModel;
import Volt.example.Volt.Shared.ServiceResponse;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    ServiceResponse getOrCreateChatRoom(UUID userId);
    ServiceResponse<ChatMessage> sendMessage(ChatMessage chatMessage);
    ServiceResponse updateMessageStatus(Long messageId,Long chatRoomId, MessageStatus status);
    ServiceResponse sendTypingNotification(UUID sender, UUID receiver, Long chatRoomId, boolean isTyping);
    ServiceResponse<List<ChatMessage>> getAllMessagesInSpecficRoom(Long roomId, SearchModel searchModel);
    ServiceResponse<List<ChatRoomListDto>> getAllChatRoomForCurrentUser(SearchModel searchModel);
}
