package Volt.example.Volt.CustomerManagement.Domain.Repositories;

import Volt.example.Volt.CustomerManagement.Domain.Entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
    RefreshToken findByJwt(String jwt);
    boolean existsByToken(String token);
}
