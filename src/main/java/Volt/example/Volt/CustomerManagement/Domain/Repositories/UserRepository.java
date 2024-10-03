package Volt.example.Volt.CustomerManagement.Domain.Repositories;

import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository<S extends User> extends JpaRepository<User, UUID> {
    User findByVerificationToken(String verificationToken);
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByEmailIgnoreCaseOrUserNameIgnoreCase(String email, String userName);
    boolean existsByEmailIgnoreCaseOrUserNameIgnoreCase(String email, String userName);
}
