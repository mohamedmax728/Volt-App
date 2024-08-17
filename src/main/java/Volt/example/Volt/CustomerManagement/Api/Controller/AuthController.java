package Volt.example.Volt.CustomerManagement.Api.Controller;

import Volt.example.Volt.CustomerManagement.Application.Dtos.User.*;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.Shared.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping(path = "/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> register(@Valid @RequestBody UserRegisterDto registerDto) {
        return authService.register(registerDto);

    }

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> login(@Valid @RequestBody UserLoginDto loginDto) {
        return authService.login(loginDto);

    }

    @PostMapping("/resendVerificationEmail")
    public ResponseEntity<ServiceResponse> resendVerificationEmail(@RequestParam String email) {
        return authService.resendVerificationEmail(email);
    }

    @PostMapping("/verify")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> verify(@RequestParam String token) {
        return authService.verify(token);
    }

    @PostMapping("/sendEmailToForgetPassword")
    public ResponseEntity<ServiceResponse<String>> sendEmailToForgetPassword(@RequestParam String email) throws Throwable {
        return authService.sendEmailToForgetPassword(email);
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ServiceResponse<String>>
    forgetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
        try {
            return authService.forgetPassword(forgetPasswordDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ServiceResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto)
             {
                 try {
                     return authService.resetPassword(resetPasswordDto);
                 } catch (Exception e) {
                     throw new RuntimeException(e);
                 }
             }

    @PostMapping("/validateRefreshToken")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> validateRefreshToken(@RequestParam String token) {
        return authService.validateRefreshToken(token);
    }
}