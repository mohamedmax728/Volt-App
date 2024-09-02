package Volt.example.Volt.CustomerManagement.Application.Interfaces;

import Volt.example.Volt.CustomerManagement.Application.Dtos.User.*;
import Volt.example.Volt.CustomerManagement.Domain.Entities.User;
import Volt.example.Volt.Shared.ServiceResponse;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AuthService {
    ServiceResponse<AuthenticationResult> login(UserLoginDto loginDto);
    ServiceResponse register(UserRegisterDto registerDto);
    ServiceResponse<AuthenticationResult> verify(String token);
    ServiceResponse resendVerificationEmail(String email);
    ServiceResponse<String> sendEmailToForgetPassword(String email) throws Throwable;
    ServiceResponse<String> forgetPassword(ForgetPasswordDto forgetPasswordDto) throws Exception;
    ServiceResponse<String> resetPassword(ResetPasswordDto resetPasswordDto) throws Exception;
    ServiceResponse<AuthenticationResult> validateRefreshToken(String token);
    String getCurrentUserEmail();
    UUID getCurrentUserId();
}
