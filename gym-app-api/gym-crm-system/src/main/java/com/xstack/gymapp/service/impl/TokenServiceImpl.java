package com.xstack.gymapp.service.impl;

import com.xstack.gymapp.exception.EntityNotFoundException;
import com.xstack.gymapp.model.dto.UserDto;
import com.xstack.gymapp.model.entity.Token;
import com.xstack.gymapp.model.entity.User;
import com.xstack.gymapp.model.enumeration.TokenType;
import com.xstack.gymapp.model.mapper.CycleAvoidingMappingContext;
import com.xstack.gymapp.model.mapper.UserMapper;
import com.xstack.gymapp.repository.TokenRepository;
import com.xstack.gymapp.service.TokenService;
import java.util.Comparator;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

  private static final String TOKEN_NOT_FOUND = "Cannot find the jwt token: ";

  private TokenRepository tokenRepository;
  private UserMapper userMapper;

  public void saveUserToken(UserDto userDto, String jwtToken) {
    Token token = Token.builder()
        .user(userMapper.toEntity(userDto, new CycleAvoidingMappingContext()))
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  public void revokeAllUserTokens(Long userId) {
    List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(userId);
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public boolean updateTokenAfterLogout(String jwt) {
    var storedToken = tokenRepository.findByToken(jwt)
        .orElseThrow(() -> new EntityNotFoundException(TOKEN_NOT_FOUND + jwt));
    storedToken.setExpired(true);
    storedToken.setRevoked(true);
    tokenRepository.save(storedToken);
    return true;
  }

  public String getValidJWTTokenByUserId(Long userId) {
    List<Token> tokens = tokenRepository.findAllValidTokensByUser(userId);
    if (tokens == null || tokens.isEmpty()) {
      return "";
    }
    Token token = tokens.stream().max(Comparator.comparing(Token::getId)).orElse(null);
    return token.getToken();
  }
}
