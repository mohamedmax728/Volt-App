package Volt.example.Volt.CustomerManagement.Domain.Entities;

import Volt.example.Volt.CustomerManagement.Domain.Enums.Role;
import Volt.example.Volt.CustomerManagement.Domain.Enums.UserStatus;
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
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
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
    @Column
    private LocalDateTime resetTokenExpires;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RefreshToken> refreshTokens;



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
