package Volt.example.Volt.Interaction.Domain.Entities.Messages;

import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.Shared.Dtos.AuditModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "chat_rooms", schema = "interaction")
@RequiredArgsConstructor
public class ChatRoom extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sender_id")
    private final UUID senderId;
    @Column(name = "recipient_id")
    private final UUID recipientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_user_id", insertable = false, updatable = false)
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_user_id", insertable = false, updatable = false)
    private User recipient;
}
