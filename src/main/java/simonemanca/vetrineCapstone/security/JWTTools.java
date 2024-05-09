package simonemanca.vetrineCapstone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.UnauthorizedException;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secretKey;

    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        if (secretKey == null) {
            throw new IllegalStateException("JWT secret key must not be null");
        }
        this.jwtParser = Jwts.parser().setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())).build();
    }

    public String createToken(String username, UUID userId) {
        try {
            Date now = new Date();
            Date validity = new Date(now.getTime() + 3600000 * 24 * 7);  // Token valido per una settimana

            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

            String token = Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .claim("userId", userId.toString())
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
            System.out.println("Token generato: " + token);
            return token;
        } catch (Exception e) {
            System.out.println("Errore nella generazione del token: " + e.getMessage());
            throw new RuntimeException("Errore nella generazione del token", e);
        }
    }

    public void verifyToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
        } catch (JwtException e) {
            throw new UnauthorizedException("Token non valido: " + e.getMessage());
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return UUID.fromString(claims.get("userId", String.class));
    }
}








