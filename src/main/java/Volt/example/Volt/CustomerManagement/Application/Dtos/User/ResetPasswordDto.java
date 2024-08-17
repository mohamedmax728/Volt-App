package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
public class ResetPasswordDto {

    @Length(min = 6, message = "password Must Be More Than 6 Charchters")
    private String password;
    private String confirmPassword;


    @AssertTrue(message = "Password Must Match")
    Boolean isPasswordMatch(){return password != null && password.equals(confirmPassword);}

}
