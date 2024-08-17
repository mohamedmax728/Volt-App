package Volt.example.Volt.CustomerManagement.Application.Interfaces;

import Volt.example.Volt.CustomerManagement.Application.Dtos.User.*;
import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.Shared.ServiceResponse;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService {
    ResponseEntity<ServiceResponse<AuthenticationResult>> login(UserLoginDto loginDto);
    ResponseEntity<ServiceResponse> register(UserRegisterDto registerDto);
    ResponseEntity<ServiceResponse<AuthenticationResult>> verify(String token);
    ResponseEntity<ServiceResponse> resendVerificationEmail(String email);
    ResponseEntity<ServiceResponse<String>> sendEmailToForgetPassword(String email) throws Throwable;
    ResponseEntity<ServiceResponse<String>> forgetPassword(ForgetPasswordDto forgetPasswordDto) throws Exception;
    ResponseEntity<ServiceResponse<String>> resetPassword(ResetPasswordDto resetPasswordDto) throws Exception;
    ResponseEntity<ServiceResponse<AuthenticationResult>> validateRefreshToken(String token);
//    String extractEmail(String token);
//    Claims extractClaims(String token);
//    boolean isValid(String token, User user);
//    String extractClaim(String token, String claimKey);
}
