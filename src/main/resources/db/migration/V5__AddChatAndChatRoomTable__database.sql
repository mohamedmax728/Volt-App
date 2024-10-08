CREATE SCHEMA IF NOT EXISTS interaction;

CREATE TABLE IF NOT EXISTS interaction.chat_rooms (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    sender_id UUID NOT NULL,
    recipient_id UUID NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NULL,
    -- Foreign keys
    CONSTRAINT fk_sender
        FOREIGN KEY (sender_id) REFERENCES user_management.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipient
        FOREIGN KEY (recipient_id) REFERENCES user_management.users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sender_id ON interaction.chat_rooms(sender_id);
CREATE INDEX IF NOT EXISTS idx_recipient_id ON interaction.chat_rooms(recipient_id);

CREATE TABLE IF NOT EXISTS interaction.chat_messages (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    status VARCHAR(10) NOT NULL,
    sender_id UUID NOT NULL,
    chat_room_id BIGINT NOT NULL,

    creation_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NULL,
    -- Foreign keys
    CONSTRAINT fk_sender
        FOREIGN KEY (sender_id) REFERENCES user_management.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_chat_room
        FOREIGN KEY (chat_room_id) REFERENCES interaction.chat_rooms(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_sender_id_msg ON interaction.chat_messages(sender_id);
CREATE INDEX IF NOT EXISTS idx_chat_room_id ON interaction.chat_messages(chat_room_id);
