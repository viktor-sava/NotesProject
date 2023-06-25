package ua.lpnu.notes.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.lpnu.notes.exception.AccessTokenNotValidException;
import ua.lpnu.notes.exception.RefreshTokenNotValidException;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtManager {

    @Value("${token.access.secret}")
    private String accessSecret;

    @Value("${token.refresh.secret}")
    private String refreshSecret;

    @Value("${token.access.duration:3600}")
    private int accessDuration;

    @Value("${token.refresh.duration:604800}")
    private int refreshDuration;

    private String createToken(String email, int duration, String secret) {
        Instant now = Instant.now();
        Instant plus = now.plusSeconds(duration);
        return Jwts.builder()
                .setIssuer("notes")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(plus))
                .setSubject(email)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public String createAccessToken(String email) {
        return createToken(email, accessDuration, accessSecret);
    }

    public String createRefreshToken(String email) {
        return createToken(email, refreshDuration, refreshSecret);
    }

    public String getUserEmail(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(accessSecret.getBytes()))
                    .build();
            Jws<Claims> jws = parser.parseClaimsJws(token);
            return jws.getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new AccessTokenNotValidException("Token is not valid");
        }
    }

    public String getUserEmailByRefreshToken(String refreshToken) {
        try {
            JwtParser parser = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(refreshSecret.getBytes()))
                    .build();
            Jws<Claims> jws = parser.parseClaimsJws(refreshToken);
            return jws.getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new RefreshTokenNotValidException("Refresh token is not valid");
        }
    }
}
