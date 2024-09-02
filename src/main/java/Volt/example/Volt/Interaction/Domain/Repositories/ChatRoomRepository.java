package Volt.example.Volt.Interaction.Domain.Repositories;

import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
