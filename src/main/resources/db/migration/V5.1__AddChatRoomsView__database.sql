CREATE VIEW chat_room_view AS
SELECT
    cr.id AS chatRoomId,
    u.id AS userId,
    CASE
        WHEN cr.sender_id = u.id THEN cr.recipient_id
        ELSE cr.sender_id
    END AS contactId,
    CASE
        WHEN cr.sender_id = u.id THEN recipient.full_name
        ELSE sender.full_name
    END AS contactName,
    MAX(cm.content) AS lastMessageContent,
    MAX(cm.creation_date) AS lastMessageDate,
    SUM(CASE WHEN cm.status = 'UNREAD' AND cm.sender_id != u.id THEN 1 ELSE 0 END) AS unreadCount
FROM
    interaction.chat_messages cm
JOIN
    interaction.chat_rooms cr ON cm.chat_room_id = cr.id
JOIN
    user_management.users sender ON sender.id = cr.sender_id
JOIN
    user_management.users recipient ON recipient.id = cr.recipient_id
JOIN
    user_management.users u ON (u.id = cr.sender_id OR u.id = cr.recipient_id)
GROUP BY
    cr.id, u.id, cr.sender_id, cr.recipient_id, sender.full_name, recipient.full_name
ORDER BY
    lastMessageDate DESC;
