package org.example.common;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.example.api.user.entity.User;
import org.example.common.dto.UserCredentials;

import java.util.Date;

public class JwtUtils {
    private final String jwtSecret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    public String generateJwtToken(User user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .claim("password", user.getPassword())
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .signWith(
                        SignatureAlgorithm.HS512,
                        TextCodec.BASE64.decode(jwtSecret)
                )
                .compact();
    }

    public UserCredentials getUserCredentialsFromJwtToken(String token) {
        Claims parsedJwt = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return new UserCredentials(parsedJwt.getSubject(), parsedJwt.get("password", String.class));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 IllegalArgumentException |
                 UnsupportedJwtException e
        ) {
            System.out.print(e.getMessage());
        }

        return false;
    }
}
