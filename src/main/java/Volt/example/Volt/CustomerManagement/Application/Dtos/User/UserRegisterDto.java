package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import Volt.example.Volt.CustomerManagement.Domain.Enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UserRegisterDto {

    @NotBlank(message = "Email cannot be blank.")
    @NotEmpty(message = "Email cannot be empty.")
    @NotNull(message = "Email cannot be blank.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @Length(min = 6, message = "Password must be at least 6 characters long.")
    @NotEmpty(message = "Password cannot be empty.")
    @NotNull(message = "Password cannot be blank.")
    private String password;

    @NotEmpty(message = "Confirm Password cannot be empty.")
    @NotNull(message = "Confirm Password cannot be blank.")
    @NotBlank(message = "Confirm Password cannot be blank.")
    private String confirmPassword;

    @NotEmpty(message = "Full Name cannot be empty.")
    @NotNull(message = "Full Name cannot be blank.")
    @NotBlank(message = "Full Name cannot be blank.")
    private String fullName;


    @NotNull(message = "Gender cannot be null.")
    private Gender gender;

    private MultipartFile image;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
