package fsts.rsi.secservices.SecurityConfig;

import fsts.rsi.secservices.Services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccountService accountService;

    // Définit la chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactive la protection CSRF car nous utilisons des tokens JWT
                .csrf(AbstractHttpConfigurer::disable)
                // Configure les règles d'autorisation pour les requêtes HTTP
                .authorizeHttpRequests(request -> request
                        // Permet l'accès public aux endpoints d'authentification
                        .requestMatchers("api/v1/auth/**").permitAll()
                        // Restreint l'accès à l'endpoint admin aux utilisateurs avec le rôle ADMIN
                        .requestMatchers("api/v1/admin").hasAuthority("ROLE_ADMIN")
                        // Restreint l'accès à l'endpoint user aux utilisateurs avec le rôle USER
                        .requestMatchers("api/v1/user").hasAuthority("ROLE_USER")
                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                )
                // Configure la gestion des sessions
                .sessionManagement(session -> session
                        // Utilise une politique sans état (stateless) car nous utilisons des JWT
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Définit le fournisseur d'authentification
                .authenticationProvider(authenticationProvider())
                // Ajoute notre filtre JWT avant le filtre d'authentification par nom d'utilisateur/mot de passe
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Construit et retourne la chaîne de filtres de sécurité
        return http.build();
    }

    // Définit le fournisseur d'authentification
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Crée un nouveau fournisseur d'authentification basé sur DAO
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        // Définit le service de détails utilisateur pour l'authentification
        authenticationProvider.setUserDetailsService(accountService.userDetailsService());
        // Retourne le fournisseur d'authentification configuré
        return authenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();


    }
}