package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
public class UserLoginDto {

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
}
