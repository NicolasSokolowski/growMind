package com.brainplus.growMind.auth;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.role.RoleRepository;
import com.brainplus.growMind.token.Token;
import com.brainplus.growMind.token.TokenRepository;
import com.brainplus.growMind.token.TokenType;
import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.role.Role;
import com.brainplus.growMind.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    Role defaultRole = roleRepository.findById(1)
        .orElseThrow(() -> new RuntimeException("Default role not found"));

    var user = AppUser.builder()
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(defaultRole)
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user, user.getId());
    var refreshToken = jwtService.generateRefreshToken(user, user.getId());
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user, user.getId());
    var refreshToken = jwtService.generateRefreshToken(user, user.getId());
    revokedAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }

  private void revokedAllUserTokens(AppUser appUser) {
    var validUserTokens = tokenRepository.findAllValidTokensByUser(appUser.getId());
    if (validUserTokens.isEmpty())
      return;

    validUserTokens.forEach(t -> {
      t.setExpired(true);
      t.setRevoked(true);
    });

    tokenRepository.saveAll(validUserTokens);
  }

  private void saveUserToken(AppUser user, String jwtToken) {
    var token = Token.builder()
        .appUser(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .revoked(false)
        .expired(false)
        .build();
    tokenRepository.save(token);
  }

  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    var userId = jwtService.extractUserIdFromToken(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
          .orElseThrow(() -> new EmptyResultDataAccessException("Email not found", 1));

      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user, userId);
        revokedAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();

        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
