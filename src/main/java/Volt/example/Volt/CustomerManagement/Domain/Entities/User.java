package Volt.example.Volt.CustomerManagement.Domain.Entities;

import Volt.example.Volt.ContentManagement.Domain.Entities.Channel;
import Volt.example.Volt.CustomerManagement.Domain.Enums.Gender;
import Volt.example.Volt.CustomerManagement.Domain.Enums.Role;
import Volt.example.Volt.CustomerManagement.Domain.Enums.UserStatus;
import Volt.example.Volt.Interaction.Domain.Entities.Follower.Follower;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatMessage;
import Volt.example.Volt.Interaction.Domain.Entities.Messages.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "users", schema = "user_management")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(name = "image_path", nullable = false)
    private String imagePath;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private byte[] passwordHash;
    @Column(nullable = false)
    private byte[] passwordSalt;
    @Column
    private String verificationToken;
    @Column
    private LocalDateTime verifiedAt;
    @Column
    private String passwordResetToken;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    @Column(nullable = false,name = "num_of_following")
    private Long numOfFollowing;
    @Column
    private LocalDateTime resetTokenExpires;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Follower> followers;
    @Column(name = "channel_id", insertable = false, updatable = false)
    private Integer channelId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "channel_id", nullable = false, unique = true)
    private Channel channel;
    @OneToMany(mappedBy = "sender",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> sentMessages;
//    @OneToMany(mappedBy = "first",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ChatRoom> chatRoomsAsFirst;
//    @OneToMany(mappedBy = "second",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ChatRoom> chatRoomsAsSecond;
//    @OneToMany(mappedBy = "recipient",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<ChatMessage> receivedMessages;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return new String(getPasswordHash());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
