package fsts.rsi.secservices.Services;

import fsts.rsi.secservices.entities.AppUser;
import fsts.rsi.secservices.entities.Approle;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService {
   AppUser addNewUser(AppUser user);
   Approle addNewrole(Approle approle);
   void AddRoleToUser(String rolename,String username);
   AppUser loadUserbyusername(String username);
   List<AppUser> getlistUsers();
   UserDetailsService userDetailsService();

}
