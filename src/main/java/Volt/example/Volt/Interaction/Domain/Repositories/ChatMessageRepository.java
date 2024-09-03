package Volt.example.Volt.Interaction.Domain.Repositories;

import Volt.example.Volt.Interaction.Application.Dtos.ChatRoomListDto;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

//    @Query(value = "select * " +
//            "from interaction.chat_messages where " +
//            "(sender_id = :senderId And recipient_id = :recipientId) Or " +
//            "(sender_id = :recipientId And recipient_id = :senderId) " +
//            "order by creation_date Desc")
//@Query("SELECT " +
//        "cm.chatRoom.id, " +
//        "u.id, " +
//        "CASE " +
//        "    WHEN cr.sender.id = :currentUserId THEN cr.recipient.fullName " +
//        "    ELSE cr.sender.fullName " +
//        "END AS otherPersonName, " +
//        "cm.content, " +
//        "MAX(cm.creationDate) AS lastMessageDate, " +
//        "SUM(CASE WHEN cm.status = com.example.enums.MessageStatus.UNREAD AND cm.sender.id != :currentUserId THEN 1 ELSE 0 END) AS unreadCount " +
//        "FROM ChatMessage cm " +
//        "JOIN cm.chatRoom cr " +
//        "JOIN User u ON (u.id = cr.sender.id OR u.id = cr.recipient.id) " +
//        "WHERE u.id = :currentUserId and (:contactName is null or :contactName = '' or :u.fullName = :contactName) " +
//        "GROUP BY cm.chatRoom.id, cm.content, cr.sender.fullName, cr.recipient.fullName " +
//        "ORDER BY MAX(cm.creationDate) DESC")
    @Query(value = "SELECT * FROM chat_room_view WHERE user_id = :currentUserId AND (:contactName is null or :contactName = '' or other_person_name = :contactName)", nativeQuery = true)
    List<ChatRoomListDto> findBySenderIdAndRecipientIdOrderByCreatedDate(
            @Param("currentUserId") UUID currentUserId, @Param("contactName") String contactName, Pageable page);

    List<ChatMessage> findByChatRoomId(Long roomId, Pageable page);
}
