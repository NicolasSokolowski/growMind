package com.brainplus.growMind.user;

import java.security.Principal;

public interface UserService {

  void changePassword(ChangePasswordRequestDto request, Principal connectedUser);

}
