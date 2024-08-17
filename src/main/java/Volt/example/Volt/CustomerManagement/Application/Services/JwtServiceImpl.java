package Volt.example.Volt.CustomerManagement.Application.Services;

import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.CustomerManagement.Domain.Repositories.RefreshTokenRepository;
import Volt.example.Volt.CustomerManagement.Domain.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.encryptionKey}")
    private String encryptionKey;
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> resolver){
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
    }
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(encryptionKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public boolean isValid(String token, User user){
        return extractEmail(token).equals(user.getEmail()) && !isTokenExpired(token) && isLoggout(token);
    }
    public boolean isLoggout(String token){
        var refreshToken = refreshTokenRepository.findByJwt(token);
               return refreshToken !=null && !refreshToken.isLoggedOut();
    }
    private boolean isTokenExpired(String token) {

        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    public String extractClaim(String token, String claimKey) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimKey, String.class);
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);

    }
}
