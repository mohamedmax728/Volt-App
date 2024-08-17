package Volt.example.Volt.CustomerManagement.Application.Services;

import Volt.example.Volt.CustomerManagement.Application.Dtos.User.*;
import Volt.example.Volt.CustomerManagement.Application.Helpers.Utility;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.CustomerManagement.Application.Services.Shared.EmailService;
import Volt.example.Volt.CustomerManagement.Application.Services.Shared.TokenService;
import Volt.example.Volt.CustomerManagement.Domain.Entities.RefreshToken;
import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.CustomerManagement.Domain.Enums.Role;
import Volt.example.Volt.CustomerManagement.Domain.Enums.UserStatus;
import Volt.example.Volt.CustomerManagement.Domain.Repositories.RefreshTokenRepository;
import Volt.example.Volt.CustomerManagement.Domain.Repositories.UserRepository;
import Volt.example.Volt.Shared.ServiceResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Value("${app.encryptionKey}")
    private String encryptionKey;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public ResponseEntity<ServiceResponse<AuthenticationResult>> login(UserLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());
        if (user == null || !verifyPasswordHash(loginDto.getPassword(),
                user.getPasswordHash(), user.getPasswordSalt())) {
            return new ResponseEntity<>(new ServiceResponse<>(null, false, "Wrong Credentials"), HttpStatus.UNAUTHORIZED);
        }
        if (user.getVerifiedAt() == null) {
            return
                    new ResponseEntity<>(new ServiceResponse<>(null, false, "Not Verified!!")
                            , HttpStatus.UNAUTHORIZED);
        }
        AuthenticationResult tokens = generateTokens(user);
        user.setStatus(UserStatus.Online);
        userRepository.save(user);
        return new ResponseEntity<>(new ServiceResponse<>(tokens, true, ""), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ServiceResponse> register(UserRegisterDto registerDto) {

        // Check if the user already exists
        boolean userExists = userRepository.existsByEmail(registerDto.getEmail().toLowerCase());
        if (userExists) {
            return new ResponseEntity<>(new ServiceResponse<>(null, false,
                    "User already exists."), HttpStatus.BAD_REQUEST);
        }

        try {
            // Create password hash and salt
            byte[] passwordHash = new byte[64];
            byte[] passwordSalt = new byte[64];
            Utility.createPasswordHash(registerDto.getPassword(), passwordHash, passwordSalt);

            // Map DTO to User entity
            User user = new User();
            user.setFirstName(registerDto.getFirstName());
            user.setLastName(registerDto.getLastName());
            user.setEmail(registerDto.getEmail());
            user.setPasswordHash(passwordHash);
            user.setPasswordSalt(passwordSalt);
            user.setVerificationToken(Utility.createRandomToken());
            user.setRole(Role.USER);
            user.setStatus(UserStatus.Offline);
            // Send OTP email
            emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

            // Save user to the database
            userRepository.save(user);

            return new ResponseEntity<>(new ServiceResponse<>(null, true,
                    "OTP sent to your email, to verify your account."), HttpStatus.OK);

        } catch (Exception ex) {
            return new ResponseEntity<>(new ServiceResponse<>(null, false,
                    "Failure to register!!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<ServiceResponse<AuthenticationResult>> verify(String token) {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            return new ResponseEntity<>(new ServiceResponse<>(null,
                    false, "Invalid Token"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        AuthenticationResult authResult = generateTokens(user);
        return new ResponseEntity<>(new ServiceResponse<>(authResult, true, "Verified Successfully")
                , HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ServiceResponse> resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(new ServiceResponse<>(
                    null, false,"Email does not exist"), HttpStatus.NOT_FOUND);
        }

        user.setVerificationToken(Utility.createRandomToken());
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        userRepository.save(user);
        return new ResponseEntity<>(
                new ServiceResponse<>(null,true
                        , "OTP sent to your email to verify your account"),HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<ServiceResponse<String>> sendEmailToForgetPassword(String email){
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return new ResponseEntity<>(
                    new ServiceResponse<>(null,
                            false, "Email does not exist"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        user.setPasswordResetToken(Utility.createRandomToken());
        user.setResetTokenExpires(LocalDateTime.now().plusDays(1));
        emailService.sendOtp(user.getEmail(), user.getPasswordResetToken());
        userRepository.save(user);

        return new ResponseEntity<>(
                new ServiceResponse<>(null,true, "OTP sent to your email")
                ,HttpStatus.OK
        );
    }

    @Transactional
    public ResponseEntity<ServiceResponse<String>> forgetPassword(ForgetPasswordDto forgetPasswordDto) throws Exception {
        User user = userRepository.findByEmail(forgetPasswordDto.getEmail());
        if (user == null) {
            return new ResponseEntity<>(
                new ServiceResponse<>(null,
        false, "Email does not exist"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (user.getPasswordResetToken() == null || !user.getPasswordResetToken().equals(forgetPasswordDto.getOtp()) || user.getResetTokenExpires().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>(
                    new ServiceResponse<>(null,
                            false, "Invalid OTP"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        byte[] passwordHash = new byte[64];
        byte[] passwordSalt = new byte[64];
        Utility.createPasswordHash(forgetPasswordDto.getPassword(), passwordHash, passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(passwordSalt);
        user.setPasswordResetToken(null);
        user.setResetTokenExpires(null);

        userRepository.save(user);
        return new ResponseEntity<>(
                new ServiceResponse<>(null,
                        true, "Your password has been changed successfully")
                , HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ServiceResponse<String>> resetPassword(ResetPasswordDto resetPasswordDto) throws Exception {
        var email = getCurrentUserId(); // Implement this method to get the current user ID
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return
                    new ResponseEntity<>(
                            new ServiceResponse<>(null,
                                    false, "User not found"), HttpStatus.INTERNAL_SERVER_ERROR
                    );
        }

        byte[] passwordHash = new byte[64];
        byte[] passwordSalt = new byte[64];
        Utility.createPasswordHash(resetPasswordDto.getPassword(), passwordHash, passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(passwordSalt);

        userRepository.save(user);
        return new ResponseEntity<>(
                new ServiceResponse<>(null,
                        true, "Your password has been changed successfully")
                ,HttpStatus.OK
        );
    }
    @Transactional
    public ResponseEntity<ServiceResponse<AuthenticationResult>> validateRefreshToken(String token) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(token);

        if (storedRefreshToken == null || storedRefreshToken.getExpiryDate().isBefore(LocalDateTime.now()) || storedRefreshToken.isUsed()) {
            // Invalid refresh token
            return new ResponseEntity<>(new ServiceResponse<>(null,
                    false, "Invalid refresh token"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Mark the token as used
        storedRefreshToken.setUsed(true);
        refreshTokenRepository.save(storedRefreshToken);

        User user = (User) userRepository.findById(storedRefreshToken.getUserId())
                .orElse(null);

        user.setStatus(UserStatus.Online);
        return new ResponseEntity<>(new ServiceResponse<>(generateTokens(user),
                true, "Token validated"), HttpStatus.OK);
    }
    @Transactional
    private AuthenticationResult generateTokens(User user) {
        String jwtToken = createJwtToken(user);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(Utility.generateRefreshToken());
        refreshToken.setJwt(jwtToken);
        refreshToken.setUser(user);
        refreshToken.setCreationDate(LocalDateTime.now());
        refreshToken.setExpiryDate(LocalDateTime.now().plusMonths(6));
        refreshTokenRepository.save(refreshToken);
        return new AuthenticationResult(jwtToken, refreshToken.getToken());



    }
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            var userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // Assuming username is used as user ID
        }

        return null; // Or throw an exception if preferred
    }
    public static boolean verifyPasswordHash(String password, byte[] passwordHash, byte[] passwordSalt) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(passwordSalt, "HmacSHA512");
            hmac.init(secretKeySpec);

            byte[] computedHash = hmac.doFinal(password.getBytes());

            return Arrays.equals(computedHash, passwordHash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error while verifying password hash", e);
        }
    }
    public String createJwtToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("roles", user.getRole());  // Assuming getRoles() returns a List<String> of roles

         return Jwts.builder()
                 .setClaims(claims)
                 .setSubject(user.getEmail())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day expiration
                 .signWith(getSigningKey())
                 .compact();
    }
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64URL.decode(encryptionKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}