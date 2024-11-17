package com.brainplus.growMind.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private static final String SECRET_KEY = "lkcbMwxtxBP0WXwKu/599OfIBZD6F4zze3LjmEXp5rSdGhycwu2rYie9MAByWrOL2Kw4lW8gK1I8o4430DUWk1LqiyPzEOeLk1lc5AQCvNh9QInla932mka5KAh0anrAwuJlU+aPUKo+jzHSUKc33OYMVBAO0xNroFq1GcM8KM38ifMGLlOyoYpTuOLkjpBa+FEG0Cakc+DpLh1Eqbb/lV9xb0/9K3lpijv2SfBpqOAp2JYOnbzVEmj7KdsS83XsXoR8SdPrGY9OL0ME3noX4F7isQmXrcpZFqrPwdYxe2Y6Q80kjjx6rDQSddieiKre9jejOtIIiqEvW652wQbG9Fpi5NuFiZ8GWi6hNbEBsB8=\n";

  public int extractUserIdFromToken(String token) {
    final Claims claims = extractAllClaims(token);
    Integer userId = claims.get("userId", Integer.class);
    if (userId == null) {
      throw new IllegalArgumentException("User ID is missing from the token");
    }
    return userId;
  }


  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails, int userId) {
    return generateToken(new HashMap<>(), userDetails, userId);
  }

  public String generateToken(
      Map<String, Object> extraClaims,
      UserDetails userDetails,
      int userId
  ) {
    extraClaims.put("userId", userId);

    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
        .signWith(getSigninKey(), SignatureAlgorithm.HS256)
        .compact();
  }
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(getSigninKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSigninKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
