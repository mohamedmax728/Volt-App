
CREATE SCHEMA IF NOT EXISTS content_management;


CREATE TABLE IF NOT EXISTS content_management.channels (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    bio TEXT NOT NULL,
    image_path TEXT NOT NULL,
    backgound_image_path TEXT NOT NULL,
    discord_url TEXT,
    x_url TEXT,
    instagram_url TEXT,
    youtube_url TEXT,
    facebook_url TEXT,
    num_of_followers BIGINT NOT NULL DEFAULT 0,
    creation_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NULL,
    user_id UUID NOT NULL UNIQUE,

    -- Foreign key constraint
    CONSTRAINT fk_user
    FOREIGN KEY (user_id) REFERENCES user_management.users(id)
    ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_channel_user_id ON content_management.channels (user_id);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_channel'
        AND conrelid = 'user_management.users'::regclass
    ) THEN
        ALTER TABLE user_management.users
        ADD CONSTRAINT fk_channel
        FOREIGN KEY (channel_id) REFERENCES content_management.channels(id)
        ON DELETE NO ACTION;
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_channel_id ON user_management.users (channel_id);

Create table if not exists content_management.categories(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(25) Not NULL
);

Create Table IF Not EXISTS content_management.category_channel(
    channel_id BIGINT Not NULL,
    category_id BIGINT Not NULL,
    PRIMARY KEY(channel_id, category_id),
    CONSTRAINT fk_category FOREIGN Key (category_id) REFERENCES content_management.categories(id) ON DELETE CASCADE,
    CONSTRAINT fk_channel FOREIGN Key (channel_id) REFERENCES content_management.channels(id) ON DELETE CASCADE
);
