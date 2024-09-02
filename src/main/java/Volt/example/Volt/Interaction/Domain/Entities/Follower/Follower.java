package Volt.example.Volt.Interaction.Domain.Entities.Follower;

import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "followers", schema = "interaction")
public class Follower {
    @EmbeddedId
    private FollowerId followerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
