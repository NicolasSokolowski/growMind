package com.brainplus.growMind.auth;

import com.brainplus.growMind.config.JwtService;
import com.brainplus.growMind.role.RoleRepository;
import com.brainplus.growMind.user.AppUser;
import com.brainplus.growMind.role.Role;
import com.brainplus.growMind.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository repository;
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
    repository.save(user);
    var jwt = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwt)
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
    var jwt = jwtService.generateToken(user);
    return AuthenticationResponse.builder()
        .token(jwt)
        .build();
  }
}
