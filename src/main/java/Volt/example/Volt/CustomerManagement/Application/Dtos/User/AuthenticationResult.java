package Volt.example.Volt.CustomerManagement.Application.Dtos.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AuthenticationResult {
    private String accessToken;
    private String refreshToken;
}
