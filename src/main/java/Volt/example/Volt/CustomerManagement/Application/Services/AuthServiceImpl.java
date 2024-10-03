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
import Volt.example.Volt.Shared.Helpers.UploadFiles;
import Volt.example.Volt.Shared.ServiceResponse;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.INTERFACES)
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${app.encryptionKey}")
    private String encryptionKey;

    @Value("${uploadfilesDir}")
    private String uploadFilesDir;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenService tokenService;

    @Transactional
    public ServiceResponse<AuthenticationResult> login( UserLoginDto loginDto) {
        Optional<User> user = userRepository.findByEmailIgnoreCase(loginDto.getEmail());
        if (user.isEmpty() || !verifyPasswordHash(loginDto.getPassword(),
                user.get().getPasswordHash(), user.get().getPasswordSalt())) {
            return new ServiceResponse<>(null, false, "Wrong Credentials.",
            "بيانات تسجيل الدخول غير صحيحة.", HttpStatus.UNAUTHORIZED);
        }
        if (user.get().getVerifiedAt() == null) {
            return
                    new ServiceResponse<>(null, false, "Not Verified!!",
                            "غير مُوثَّق!!"
                            , HttpStatus.UNAUTHORIZED);
        }
        AuthenticationResult tokens = generateTokens(user.get());
        user.get().setStatus(UserStatus.Online);
        userRepository.save(user.get());
        return new ServiceResponse<>(tokens, true, "","", HttpStatus.OK);
    }

    @Transactional
    public ServiceResponse register(@NotNull UserRegisterDto registerDto) {

        boolean userExists = userRepository.existsByEmailIgnoreCaseOrUserNameIgnoreCase
                (registerDto.getEmail().toLowerCase(), registerDto.getUserName().toLowerCase());
        if (userExists) {
            return new ServiceResponse<>(null, false,
                    "User already exists.", "هذا المستخدم موجود بالفعل.", HttpStatus.BAD_REQUEST);
        }
        if(!UploadFiles.isImageFile(registerDto.getImage())){
            return new ServiceResponse<>(null, false,
                    "please enter correct image", "من فضلك ادخل صورة", HttpStatus.BAD_REQUEST);
        }
        try {
            byte[] passwordHash = new byte[64];
            byte[] passwordSalt = new byte[64];
            Utility.createPasswordHash(registerDto.getPassword(), passwordHash, passwordSalt);

            File uploadDir = new File(uploadFilesDir + "/ProfilePicture");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePath = UploadFiles.uploadProfileImages(registerDto.getImage()
                    ,uploadFilesDir + "/ProfilePicture/");

            User user = new User();
            user.setImagePath(filePath.toString());
            user.setFullName(registerDto.getFullName());
            user.setGender(registerDto.getGender());
            user.setEmail(registerDto.getEmail());
            user.setUserName(registerDto.getUserName());
            user.setPasswordHash(passwordHash);
            user.setPasswordSalt(passwordSalt);
            user.setNumOfFollowing(0L);
            user.setVerificationToken(Utility.createRandomToken());
            user.setRole(Role.USER);
            user.setStatus(UserStatus.Offline);
            emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());

            userRepository.save(user);

            return new ServiceResponse<>(null, true,
                    "OTP sent to your email, to verify your account.",
                    "تم إرسال رمز التحقق إلى بريدك الإلكتروني لتأكيد حسابك.", HttpStatus.OK);

        } catch (Exception ex) {
            return new ServiceResponse<>(null, false,
                    "Failure to register!!", "فشل في التسجيل!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ServiceResponse<AuthenticationResult> verify(String token) {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            return new ServiceResponse<>(null,
                    false, "Invalid Token.", "رمز غير صالح.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setVerifiedAt(LocalDateTime.now());
        user.setStatus(UserStatus.Online);
        userRepository.save(user);

        AuthenticationResult authResult = generateTokens(user);
        return new ServiceResponse<>(authResult, true, "Verified Successfully."
        ,"تم التحقق بنجاح."
                , HttpStatus.OK);
    }

    @Transactional
    public ServiceResponse resendVerificationEmail(String email) {
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email);
        if (optUser.isEmpty()) {
            return new ServiceResponse<>(
                    null, false,"Email does not exist.",
                    "البريد الإلكتروني غير موجود.", HttpStatus.NOT_FOUND);
        }
        var user = optUser.get();
        user.setVerificationToken(Utility.createRandomToken());
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        userRepository.save(user);
        return
                new ServiceResponse<>(null,true
                        , "OTP sent to your email to verify your account.",
                        "تم إرسال رمز التحقق إلى بريدك الإلكتروني لتأكيد حسابك.",HttpStatus.OK
        );
    }

    @Transactional
    public ServiceResponse<String> sendEmailToForgetPassword(String email){
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email);

        if (optUser.isEmpty()) {
            return
                    new ServiceResponse<>(null,
                            false, "Email does not exist.",
                            "البريد الإلكتروني غير موجود.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var user = optUser.get();
        user.setPasswordResetToken(Utility.createRandomToken());
        user.setResetTokenExpires(LocalDateTime.now().plusDays(1));
        emailService.sendOtp(user.getEmail(), user.getPasswordResetToken());
        userRepository.save(user);

        return
                new ServiceResponse<>(null,true, "OTP sent to your email.",
                        "تم إرسال رمز التحقق إلى بريدك الإلكتروني."
                ,HttpStatus.OK
        );
    }

    @Transactional
    public ServiceResponse<String> verifyForgetPasswordOTP(ForgetPasswordVerificationOtpDto forgetPasswordVerificationOtpDto){
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(forgetPasswordVerificationOtpDto.getEmail());
        if (optUser.isEmpty()) {
            return
                    new ServiceResponse<>(null,
                            false, "Email does not exist.", "البريد الإلكتروني غير موجود.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var user = optUser.get();
        if (user.getPasswordResetToken() == null || !user.getPasswordResetToken().equals(forgetPasswordVerificationOtpDto.getOtp()) || user.getResetTokenExpires().isBefore(LocalDateTime.now())) {
            return
                    new ServiceResponse<>(null,
                            false, "Invalid OTP.",
                            "رمز التحقق غير صالح.",
                            HttpStatus.INTERNAL_SERVER_ERROR
                    );
        }
        user.setPasswordResetToken(null);
        user.setResetTokenExpires(null);
        userRepository.save(user);
        return
                new ServiceResponse<>(null,
                        true, "Verified successfully.",
                        "تم التحقق بنجاح."
                        , HttpStatus.OK);
    }

    @Transactional
    public ServiceResponse<String> forgetPassword(ForgetPasswordDto forgetPasswordDto) throws Exception {
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(forgetPasswordDto.getEmail());
        if (optUser.isEmpty()) {
            return
                new ServiceResponse<>(null,
        false, "Email does not exist.", "البريد الإلكتروني غير موجود.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        var user = optUser.get();

        byte[] passwordHash = new byte[64];
        byte[] passwordSalt = new byte[64];
        Utility.createPasswordHash(forgetPasswordDto.getPassword(), passwordHash, passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(passwordSalt);
        userRepository.save(user);
        return
                new ServiceResponse<>(null,
                        true, "Your password has been changed successfully.",
                        "تم تغيير كلمة المرور بنجاح."
                , HttpStatus.OK);
    }

    @Transactional
    public ServiceResponse<String> resetPassword(ResetPasswordDto resetPasswordDto) throws Exception {
        var email = getCurrentUserEmail(); // Implement this method to get the current user ID
        Optional<User> optUser = userRepository.findByEmailIgnoreCase(email);
        if (optUser == null) {
            return
                            new ServiceResponse<>(null,
                                    false, "User not found.",
                                    "المستخدم غير موجود.", HttpStatus.INTERNAL_SERVER_ERROR
                    );
        }
        var user = optUser.get();
        byte[] passwordHash = new byte[64];
        byte[] passwordSalt = new byte[64];
        Utility.createPasswordHash(resetPasswordDto.getPassword(), passwordHash, passwordSalt);
        user.setPasswordHash(passwordHash);
        user.setPasswordSalt(passwordSalt);

        userRepository.save(user);
        return
                new ServiceResponse<>(null,
                        true, "Your password has been changed successfully.",
                        "تم تغيير كلمة المرور بنجاح."
                ,HttpStatus.OK
        );
    }
    @Transactional
    public ServiceResponse<AuthenticationResult> validateRefreshToken(String token) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(token);

        if (storedRefreshToken == null || storedRefreshToken.getExpiryDate().isBefore(LocalDateTime.now()) || storedRefreshToken.isUsed()) {
            // Invalid refresh token
            return new ServiceResponse<>(null,
                    false, "Invalid refresh token.", "رمز التحديث غير صالح."
            , HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Mark the token as used
        storedRefreshToken.setUsed(true);
        refreshTokenRepository.save(storedRefreshToken);

        User user = (User) userRepository.findById(storedRefreshToken.getUserId())
                .orElse(null);

        user.setStatus(UserStatus.Online);
        userRepository.save(user);
        return new ServiceResponse<>(generateTokens(user),
                true, "Token validated.", "تم التحقق من الرمز.", HttpStatus.OK);
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
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            var userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // Assuming username is used as user ID
        }
        return null; // Or throw an exception if preferred
    }
    public UUID getCurrentUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            var userDetails = (User) authentication.getPrincipal();
            return userDetails.getId(); // Assuming username is used as user ID
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