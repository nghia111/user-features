package nghia_le.user_features.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private Key getSigningKey() {
        // cho vào application.yml
        String jwtSecret = "SuperLongSecretKeyForHS512_0123456789abcdefghijklmnopqrstuvwxyz!@#$%^&*()_+";
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateAccessToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // 1h
        long jwtExpirationMs = 3600000;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        // 1 ngày
        long refreshTokenExpirationMs = 86400000;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String getRoleFromToken(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

}
