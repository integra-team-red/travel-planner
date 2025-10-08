package cloudflight.integra.backend.security;

import java.security.NoSuchAlgorithmException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public static final long TOKEN_VALIDITY = ChronoUnit.DAYS.getDuration()
            .toMillis();
    private final SecretKey SECRET = KeyGenerator.getInstance("HmacSHA256")
            .generateKey();

    public JwtService() throws NoSuchAlgorithmException {
    }

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        var userRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .orElse("ROLE_user");

        Date now = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .claim("role", userRole.substring("ROLE_".length()))
                .subject(email)
                .issuedAt(now)
                .expiration(getExpiration(now))
                .signWith(SECRET)
                .compact();
    }

    private static Date getExpiration(Date now) {
        return new Date(now.getTime() + TOKEN_VALIDITY);
    }

    public boolean isTokenValid(String token) {
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (JwtException exception) {
            return false;
        }
        return !isTokenExpired(claims);
    }

    public String extractEmailFromToken(String token) throws JwtException {
        if (!isTokenValid(token)) {
            throw new JwtException("Invalid token");
        }
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(SECRET)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(Claims tokenPayload) {
        return tokenPayload.getExpiration()
                .before(new Date());
    }
}
