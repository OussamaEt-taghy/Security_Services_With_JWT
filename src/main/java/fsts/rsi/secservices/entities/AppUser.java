package fsts.rsi.secservices.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity @NoArgsConstructor @AllArgsConstructor @Data
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)

    private List<Approle> roles=new ArrayList<>();

    // This method returns the permissions or roles that the user has.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertir la liste de rôles (List<Approle>) en un flux pour traitement
        return roles.stream()
                // Pour chaque rôle, créer une nouvelle autorité avec le nom du rôle
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                // Collecter les résultats dans une liste de GrantedAuthority
                .collect(Collectors.toList());
    }

    //checks whether the user's account is expired or not.
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }
    //checks whether the user's account is locked or not.
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }
   //vérifie si les informations d'identification de l'utilisateur (par exemple, le mot de passe) sont expirées
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
    //checks whether the user's account is activated or not.
    @Override
    public boolean isEnabled() {
        return false;
    }
}
