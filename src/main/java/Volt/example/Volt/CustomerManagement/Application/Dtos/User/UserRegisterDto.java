package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

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

    @NotEmpty(message = "First Name cannot be empty.")
    @NotNull(message = "First Name cannot be blank.")
    @NotBlank(message = "First Name cannot be blank.")
    private String firstName;

    @NotEmpty(message = "Last Name cannot be empty.")
    @NotNull(message = "Last Name cannot be blank.")
    @NotBlank(message = "Last Name cannot be blank.")
    private String lastName;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}
