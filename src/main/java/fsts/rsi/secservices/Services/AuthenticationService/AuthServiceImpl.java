package fsts.rsi.secservices.Services.AuthenticationService;

import fsts.rsi.secservices.DTO.SignUpRequest;
import fsts.rsi.secservices.entities.AppUser;
import fsts.rsi.secservices.entities.Approle;
import fsts.rsi.secservices.repository.Approlerepo;
import fsts.rsi.secservices.repository.AppuserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final Approlerepo approlerepo;
    private final PasswordEncoder passwordEncoder;
    private final AppuserRepo appuserRepo;

    @Override
    public AppUser signUp(SignUpRequest signUpRequest) {
        // Vérifier si l'utilisateur existe déjà
        Optional<AppUser> existingUser = Optional.ofNullable(appuserRepo.findByUsername(signUpRequest.getUsername()));
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Cet utilisateur existe déjà");
        }

        // Créer un nouvel utilisateur
        AppUser appUser = new AppUser();
        appUser.setUsername(signUpRequest.getUsername());
        appUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // Chercher le rôle "ROLE_USER" dans la base de données
        Approle approle = approlerepo.findByName("ROLE_USER");
        if (approle == null) {
            throw new IllegalStateException("Le rôle USER n'existe pas");
        }

        // Ajouter le rôle à l'utilisateur
        appUser.getRoles().add(approle);

        // Sauvegarder l'utilisateur dans la base de données
        appuserRepo.save(appUser);

        return appUser;
    }
}
