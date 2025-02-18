package fsts.rsi.secservices.Services.JWT;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String extractUsername(String token);

    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token,UserDetails userDetails);
}
