create or replace procedure delete_followers_update_channels_and_users(
    p_user_id UUID,
    p_unfollower_ids int[]
)
language plpgsql
as $$
begin
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
        WHERE channel_id = ANY(p_unfollower_ids)
    )
    WHERE id = ANY(p_unfollower_ids);
end
$$