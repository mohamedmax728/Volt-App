package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Getter
public class ForgetPasswordDto {
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

    @AssertTrue(message = "password must be equals confirm password.")
    boolean isValid(){
        return password != null && password.equals(confirmPassword);
    }
}
