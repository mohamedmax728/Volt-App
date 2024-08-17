package Volt.example.Volt.CustomerManagement.Domain.Helpers;

public class TokenData {
    private final String userId;
    private final String token;

    public TokenData(String userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
