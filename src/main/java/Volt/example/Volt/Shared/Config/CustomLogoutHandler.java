package Volt.example.Volt.Shared.Config;


import Volt.example.Volt.CustomerManagement.Domain.Repositories.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid JWT token");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        String token = authHeader.substring(7);
        var storedToken = refreshTokenRepository.findByJwt(token);

        if(storedToken != null) {
            storedToken.setLoggedOut(true);
            refreshTokenRepository.save(storedToken);
        }
    }
}