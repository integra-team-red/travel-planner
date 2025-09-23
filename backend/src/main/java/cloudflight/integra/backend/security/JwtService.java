package cloudflight.integra.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey SECRET;

    // TODO(MC): Maybe update this to use asymmetric keys and get the key from
    // env.properties
    public JwtService() throws NoSuchAlgorithmException {
        SECRET = KeyGenerator.getInstance("HmacSHA256").generateKey();
    }

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        var userRole = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .orElse("ROLE_user");

        int validStateDurationInMillis = 30 * 60 * 1000;
        return Jwts.builder()
                .claim("role", userRole.substring("ROLE_".length()))
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + validStateDurationInMillis))
                .signWith(SECRET)
                .compact();
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
        return Jwts.parser().verifyWith(SECRET).build().parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(Claims tokenPayload) {
        return tokenPayload.getExpiration().before(new Date());
    }
}
