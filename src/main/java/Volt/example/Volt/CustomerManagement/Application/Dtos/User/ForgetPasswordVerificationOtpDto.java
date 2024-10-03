package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
public class ForgetPasswordVerificationOtpDto {
    @NotBlank(message = "Email cannot be blank.")
    @NotEmpty(message = "Email cannot be empty.")
    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email should be valid.")
    private String email;
    @NotBlank(message = "OTP cannot be blank.")
    @NotEmpty(message = "OTP cannot be empty.")
    @NotNull(message = "OTP cannot be null.")
    @Length(min = 6, max = 6, message = "OTP must be exactly 6 characters long.")
    private String otp;
}
