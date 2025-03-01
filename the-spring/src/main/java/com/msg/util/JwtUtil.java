package com.msg.util;

import static com.msg.constant.ErrorCode.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  private static final String SECRET_KEY_STRING =
      "your-secret-key-string-here-make-it-long-and-secure";
  private final SecretKey secretKey =
      Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));

  public String generateToken(String username) {
    long expirationTime = 1000 * 60 * 60 * 10; // 10 hours
    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
        .signWith(secretKey)
        .compact();
  }

  public void validateJwt(String header, String username) throws BadRequestException {
    if (header == null || !header.startsWith("Bearer ")) {
      throw new BadRequestException(INVALID_HEADER);
    }

    try {
      String token = header.substring(7);
      JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
      Jws<Claims> jws = jwtParser.parseClaimsJws(token);
      String sub = jws.getBody().get("sub").toString();

      if (!sub.equals(username)) {
        throw new BadRequestException(INVALID_USER);
      }

    } catch (JwtException | IllegalArgumentException e) {
      throw new BadRequestException(INVALID_TOKEN);
    }
  }
}
