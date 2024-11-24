package fsts.rsi.secservices.Services.AuthenticationService;

import fsts.rsi.secservices.DTO.SignUpRequest;
import fsts.rsi.secservices.entities.AppUser;

public interface AuthService {
    AppUser signUp(SignUpRequest signUpRequest);
}
