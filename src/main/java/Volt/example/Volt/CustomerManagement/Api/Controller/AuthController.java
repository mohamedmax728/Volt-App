package Volt.example.Volt.CustomerManagement.Api.Controller;

import Volt.example.Volt.CustomerManagement.Application.Dtos.User.*;
import Volt.example.Volt.CustomerManagement.Application.Interfaces.AuthService;
import Volt.example.Volt.Shared.ServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;

@Validated
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ExecutorService virtualThreadExecutor;
    @RequestMapping(method = RequestMethod.POST,
            path = "/register",  consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ServiceResponse> register(@Validated @ModelAttribute UserRegisterDto registerDto) {
        return ResponseEntity.ok(authService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> login(@Valid @RequestBody UserLoginDto loginDto) {

            return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/resendVerificationEmail")
    public ResponseEntity<ServiceResponse> resendVerificationEmail(@RequestParam String email) {
        return ResponseEntity.ok(authService.resendVerificationEmail(email));
    }

    @PostMapping("/verify")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> verify(@RequestParam String token) {
        return ResponseEntity.ok(authService.verify(token));
    }

    @PostMapping("/sendEmailToForgetPassword")
    public ResponseEntity<ServiceResponse<String>> sendEmailToForgetPassword(@RequestParam String email) throws Throwable {
        return ResponseEntity.ok(authService.sendEmailToForgetPassword(email));
    }

    @PostMapping("/verifyForgetPasswordOTP")
    public ResponseEntity<ServiceResponse<String>>
    verifyForgetPasswordOTP(
            @Valid @RequestBody ForgetPasswordVerificationOtpDto VerificationOtpDto
    ) {
        try {
            return ResponseEntity.ok(authService.verifyForgetPasswordOTP(VerificationOtpDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/forgetPassword")
    public ResponseEntity<ServiceResponse<String>>
    forgetPassword(@Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {
        try {
            return ResponseEntity.ok(authService.forgetPassword(forgetPasswordDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ServiceResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto)
             {
                 try {
                     return ResponseEntity.ok(authService.resetPassword(resetPasswordDto));
                 } catch (Exception e) {
                     throw new RuntimeException(e);
                 }
             }

    @PostMapping("/validateRefreshToken")
    public ResponseEntity<ServiceResponse<AuthenticationResult>> validateRefreshToken(@RequestParam String token) {
        return ResponseEntity.ok(authService.validateRefreshToken(token));
    }
}