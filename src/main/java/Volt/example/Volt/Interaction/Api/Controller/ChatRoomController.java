package Volt.example.Volt.Interaction.Api.Controller;

import Volt.example.Volt.Interaction.Application.Interfaces.ChatService;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import Volt.example.Volt.Shared.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/mobile/interaction/chatRoom")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatService chatService;

    @PostMapping("getOrCreateChatRoom")
    public ResponseEntity<ServiceResponse<ChatMessage>> getOrCreateChatRoom(UUID userID){
        return ResponseEntity.ok(chatService.getOrCreateChatRoom(userID));
    }
}
