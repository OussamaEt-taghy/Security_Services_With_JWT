package fsts.rsi.secservices.Controller;

import fsts.rsi.secservices.DTO.SignUpRequest;
import fsts.rsi.secservices.Services.AuthenticationService.AuthService;
import fsts.rsi.secservices.entities.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<AppUser> signup(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
}
