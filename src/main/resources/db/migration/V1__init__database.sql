
CREATE SCHEMA IF NOT EXISTS user_management;

CREATE TABLE IF NOT EXISTS user_management.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(50) NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    user_name TEXT NOT NULL UNIQUE,
    image_path VARCHAR(80) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    role VARCHAR(20) NOT NULL,
    num_of_following BIGINT NOT NULL DEFAULT 0,
    password_hash BYTEA NOT NULL,
    password_salt BYTEA NOT NULL,
    verification_token VARCHAR(255),
    verified_at TIMESTAMP,
    password_reset_token VARCHAR(255),
    reset_token_expires TIMESTAMP,
    channel_id BIGINT
);

CREATE INDEX IF NOT EXISTS idx_channel_num_of_following ON user_management.users (num_of_following);

CREATE TABLE IF NOT EXISTS user_management.refresh_tokens (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    token TEXT NOT NULL,
    jwt TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT false,
    loggedOut BOOLEAN NOT NULL DEFAULT false,
    user_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user_management.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_publication (completion_date timestamp(6) with time zone, publication_date timestamp(6) with time zone, id uuid not null, event_type varchar(255), listener_id varchar(255), serialized_event varchar(255), primary key (id))
