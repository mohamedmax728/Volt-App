package Volt.example.Volt.Interaction.Domain.Repositories;

import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {

    @EntityGraph(attributePaths = "channel")
    Page<Channel> findChannelsByUserId(UUID userId, Pageable pageable);

    @Modifying
    @Transactional
//    @Query(value = "Delete from interaction.followers where user_id = :userId and channel_id in :unfollowerIds; " +
//            "update user_management.users set num_of_following = (" +
//            "select count(*) from interaction.followers where user_id = :userId) " +
//            "where id = :userid; " +
//            "update content_management.channels set num_of_followers = (" +
//            "select count(*) from interaction.followers where channel_id = ANY(:unfollowerIds)) " +
//            "where id = ANY(:unfollowerIds);", nativeQuery = true)
    @Procedure(procedureName = "delete_followers_update_channels_and_users")
    void deleteByUserIdAndChannelIdInAndUpdateNumOfFollowingAndFollowers
            (@Param("userId") UUID userId,@Param("unfollowerIds") Set<Integer> unFollowers);
}
