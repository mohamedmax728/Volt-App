package Volt.example.Volt.Interaction.Domain.Repositories;

import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Integer> {



    @EntityGraph(attributePaths = "channel")
    @Query("select f from Follower f join f.channel c " +
            "where lower(c.name) like lower(concat('%', :channelName, '%')) " +
            "and f.followerId.userId = :userId")
    Page<Follower> findFollowersByChannelNameAndUserId(@Param("userId") UUID userId,
                                        @Param("channelName") String channelName,
                                        Pageable pageable);


//    @Query(value = "Delete from interaction.followers where user_id = :userId and channel_id in :unfollowerIds; " +
//            "update user_management.users set num_of_following = (" +
//            "select count(*) from interaction.followers where user_id = :userId) " +
//            "where id = :userid; " +
//            "update content_management.channels set num_of_followers = (" +
//            "select count(*) from interaction.followers where channel_id = ANY(:unfollowerIds)) " +
//            "where id = ANY(:unfollowerIds);", nativeQuery = true)
    @Modifying
    @Transactional
    @Procedure(procedureName = "AddAndDelete_followers_update_channels_and_users")
    void AddAndDeleteByUserIdAndChannelIdInAndUpdateNumOfFollowingAndFollowers
            (@Param("p_user_id") UUID userId, @Param("p_follower_ids") int[] followers,
             @Param("p_unfollower_ids") int[] unFollowers);
}
