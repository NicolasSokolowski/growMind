package com.brainplus.growMind.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public void changePassword(ChangePasswordRequestDto request, Principal connectedUser) {

    var user = (AppUser) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
      throw new IllegalStateException("Wrong password");
    }

    if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
      throw new IllegalStateException("Passwords are not the same");
    }

    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    userRepository.save(user);
  }
}