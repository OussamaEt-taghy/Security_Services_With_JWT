package fsts.rsi.secservices.Services;

import fsts.rsi.secservices.entities.AppUser;
import fsts.rsi.secservices.entities.Approle;
import fsts.rsi.secservices.repository.Approlerepo;
import fsts.rsi.secservices.repository.AppuserRepo;
import jakarta.transaction.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private AppuserRepo appuserRepo;
    private Approlerepo approlerepo;

    public AccountServiceImpl(AppuserRepo appuserRepo, Approlerepo approlerepo) {
        this.appuserRepo = appuserRepo;
        this.approlerepo = approlerepo;

    }

    @Override
    public AppUser addNewUser(AppUser user) {
        return appuserRepo.save(user);
    }

    @Override
    public Approle addNewrole(Approle approle) {

        return approlerepo.save(approle);
    }

    @Override
    public void AddRoleToUser(String rolename, String username) {
        AppUser appUser = appuserRepo.findByUsername(username);
        Approle approle= approlerepo.findByName(rolename);
        appUser.getRoles().add(approle);

    }

    @Override
    public AppUser loadUserbyusername(String username) {

        return appuserRepo.findByUsername(username) ;
    }

    @Override
    public List<AppUser> getlistUsers() {

        return appuserRepo.findAll();
    }

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                AppUser appUser = appuserRepo.findByUsername(username);
                // Si l'utilisateur n'est pas trouvé, on gère ce cas
                if (appUser == null) {
                    throw new UsernameNotFoundException("User not found with username: " + username);
                }
                // Si trouvé, retourne l'utilisateur (assure-toi que AppUser implémente UserDetails)
                return appUser;
            }
        };

    }
}
