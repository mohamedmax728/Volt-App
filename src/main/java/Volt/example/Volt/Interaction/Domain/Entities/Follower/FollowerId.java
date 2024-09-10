package Volt.example.Volt.Interaction.Domain.Entities.Follower;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class FollowerId implements Serializable {
    @Column(name = "channel_id")
    private int channelId;
    @Column(name = "user_id",columnDefinition = "UUID")
    private UUID userId;
}
