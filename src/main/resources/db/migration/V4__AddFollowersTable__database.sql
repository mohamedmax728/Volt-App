
create schema if not exists interaction;
create table if not exists interaction.followers(
    user_id UUID not null,
    channel_id BIGINT not null,
    primary key(user_id,channel_id),
    CONSTRAINT fk_user foreign key (user_id) references user_management.users(id) on delete cascade,
    CONSTRAINT fk_channel foreign key (channel_id) references content_management.channels(id) on delete cascade
);

create index if not exists idx_channel_id on interaction.followers(channel_id);
