package com.example.storeapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    private static final String SECRET_KEY = "eW91cl9zdXBlcl9zZWNyZXRfa2V5X2NoYW5nZV90aGlz"; // use env in prod
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24; // 24 hours

    /**
     * Generate JWT token for a user
     */
    public static String generateToken(UUID userId, String email) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Parse and verify JWT token
     */
    public static JwtPayload verifyToken(String token) throws Exception {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            UUID userId = UUID.fromString(claims.getSubject());
            String email = claims.get("email", String.class);

            return new JwtPayload(userId, email);

        } catch (ExpiredJwtException e) {
            throw new Exception("Token expired");
        } catch (SignatureException e) {
            throw new Exception("Invalid token signature");
        } catch (Exception e) {
            throw new Exception("Invalid token");
        }
    }

    /**
     * Payload class
     */
    public static class JwtPayload {
        private final UUID userId;
        private final String email;

        public JwtPayload(UUID userId, String email) {
            this.userId = userId;
            this.email = email;
        }

        public UUID getUserId() { return userId; }
        public String getEmail() { return email; }
    }
}