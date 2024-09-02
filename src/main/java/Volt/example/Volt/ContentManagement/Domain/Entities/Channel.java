package Volt.example.Volt.ContentManagement.Domain.Entities;

import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.Follower;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "channels", schema = "content_management")
@EntityListeners(AuditingEntityListener.class)
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private String bio;
    @Column(nullable = false,name = "discord_url")
    private String discordUrl;
    @Column(nullable = false,name = "x_url")
    private String xUrl;
    @Column(nullable = false,name = "instagram_url")
    private String instagramUrl;
    @Column(nullable = false,name = "youtube_url")
    private String youtubeUrl;
    @Column(nullable = false,name = "facebook_url")
    private String facebookUrl;
    @Column(nullable = false,name = "num_of_followers")
    private Long numOfFollowers;

    @Column(nullable = false,name = "creation_date")
    @CreatedDate
    private LocalDateTime creationDate;
    @Column(name = "modified_date")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    @ManyToMany
    @JoinTable(
            schema = "content_management",
            name = "category_channel",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Follower> followers;
}
