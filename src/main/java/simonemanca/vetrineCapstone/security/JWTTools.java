package simonemanca.vetrineCapstone.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import simonemanca.vetrineCapstone.entities.User;
import simonemanca.vetrineCapstone.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;


import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secretKey;

    public String createToken(String username, UUID userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000 * 24 * 7);  // Token valido per una settimana

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .claim("userId", userId.toString())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public void verifyToken(String token) {
        JwtParserBuilder jwtParserBuilder = Jwts.parser().setSigningKey(secretKey.getBytes());
        JwtParser jwtParser = jwtParserBuilder.build(); // Otteniamo un'istanza di JwtParser
        jwtParser.parseClaimsJws(token);
    }

    public UUID getUserIdFromToken(String token) {
        JwtParserBuilder jwtParserBuilder = Jwts.parser().setSigningKey(secretKey.getBytes());
        JwtParser jwtParser = jwtParserBuilder.build(); // Otteniamo un'istanza di JwtParser
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        return UUID.fromString(claims.get("userId", String.class));
    }

}






