package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class JwtServiceImpl implements JwtService {

  private final String secretKey;
  private final long jwtExpiration;
  private final long refreshExpiration;

  public JwtServiceImpl(@Value("${application.security.jwt.secret-key}") String secretKey,
      @Value("${application.security.jwt.expiration}") long jwtExpiration,
      @Value("${application.security.jwt.refresh-token.expiration}") long refreshExpiration) {
    this.secretKey = secretKey;
    this.jwtExpiration = jwtExpiration;
    this.refreshExpiration = refreshExpiration;
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  @Override
  public String generateToken(UserDto userDto) {
    UserDetails userDetails = User.withUsername(userDto.getUsername())
        .password(userDto.getPassword())
        .roles()
        .build();
    return generateToken(new HashMap<>(), userDetails);
  }

  @Override
  public String generateRefreshToken(UserDto userDto) {
    UserDetails userDetails = User.withUsername(userDto.getUsername())
        .password(userDto.getPassword())
        .roles()
        .build();
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails,
      long jwtExpiration) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    Claims claims = null;
    try {
      claims = extractAllClaims(token);
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token).getBody();
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}
