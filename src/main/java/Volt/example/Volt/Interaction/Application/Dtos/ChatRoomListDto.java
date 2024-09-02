package Volt.example.Volt.Interaction.Application.Dtos;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class ChatRoomListDto {
    private Long chatRoomId;
    private UUID userId;
    private UUID contactId;
    private String contactName;
    private String lastMessageContent;
    private LocalDateTime lastMessageDate;
    private Long unreadCount;
}
