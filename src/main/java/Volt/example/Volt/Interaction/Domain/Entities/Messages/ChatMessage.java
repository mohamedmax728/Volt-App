package Volt.example.Volt.Interaction.Domain.Entities.Messages;

import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.Interaction.Domain.Enums.MessageStatus;
import Volt.example.Volt.Interaction.Domain.Enums.MessageType;
import Volt.example.Volt.Shared.Dtos.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "chat_messages", schema = "interaction")
public class ChatMessage extends AuditModel {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false, name = "sender_id")
    private UUID senderId;
//    @Column(nullable = false, name = "recipient_id")
//    private UUID recipientId;
    @Column(nullable = false, name = "chat_room_id")
    private Long chatRoomId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status;

//    @Column(nullable = false,name = "creation_date")
//    @CreatedDate
//    private LocalDateTime creationDate;
//    @Column(name = "modified_date")
//    @LastModifiedDate
//    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "recipient_id", insertable = false, updatable = false)
//    private User recipient;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", insertable = false, updatable = false)
    private ChatRoom chatRoom;
}
