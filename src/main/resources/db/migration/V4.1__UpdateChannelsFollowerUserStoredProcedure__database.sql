CREATE OR REPLACE PROCEDURE AddAndDelete_followers_update_channels_and_users(
    p_user_id UUID,
    p_follower_ids INT[],
    p_unfollower_ids INT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    item INT;
BEGIN
    FOR item IN SELECT unnest(p_follower_ids)
    LOOP
        INSERT INTO interaction.followers (user_id, channel_id)
        VALUES (p_user_id, item)
        ON CONFLICT (user_id, channel_id) DO NOTHING;
    END LOOP;

    DELETE FROM interaction.followers
    WHERE user_id = p_user_id
      AND channel_id = ANY(p_unfollower_ids);

    UPDATE user_management.users
    SET num_of_following = (
        SELECT COUNT(*)
        FROM interaction.followers
        WHERE user_id = p_user_id
    )
    WHERE id = p_user_id;

    UPDATE content_management.channels
    SET num_of_followers = (
        SELECT COUNT(*)
        FROM interaction.followers
        WHERE channel_id = content_management.channels.id
    )
    WHERE id = ANY(p_unfollower_ids) or id = ANY(p_follower_ids);
END;
$$;
