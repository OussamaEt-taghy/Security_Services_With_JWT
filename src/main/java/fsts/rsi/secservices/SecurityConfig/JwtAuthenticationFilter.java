package fsts.rsi.secservices.SecurityConfig;

import fsts.rsi.secservices.Services.AccountService;
import fsts.rsi.secservices.Services.JWT.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final AccountService accountService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Récupère le header "Authorization" de la requête HTTP.
        final String authHeader = request.getHeader("Authorization");
        final String jwt; // Variable pour stocker le token JWT extrait du header.
        final String userEmail; // Variable pour stocker l'email de l'utilisateur extrait du token.

        // Vérifie si le header d'autorisation est vide ou s'il ne commence pas par "Bearer ".
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            // Si le header est vide ou ne commence pas par "Bearer ", passe au filtre suivant sans authentification.
            // "Bearer" est un schéma d'authentification utilisé pour indiquer que le client présente un jeton JWT (porteur du jeton).
            // "Bearer " au début du header permet à l'application de distinguer les types d'authentification et de traiter les jetons JWT de manière appropriée.
            filterChain.doFilter(request, response);
            return;
        }

        // Extrait le token JWT du header d'autorisation (on ignore les 7 premiers caractères : "Bearer ").
        jwt = authHeader.substring(7);

        // Extrait l'email de l'utilisateur à partir du token JWT grâce au service JWT.
        userEmail = jwtService.extractUsername(jwt);

        // Vérifie si l'email est vide ET si aucun utilisateur n'est déjà authentifié dans le contexte de sécurité.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Si l'email est présent dans le token et aucun utilisateur n'est actuellement authentifié,
            // Charge les détails de l'utilisateur à partir du service AccountService
            UserDetails userDetails = accountService.loadUserbyusername(userEmail);

            // Vérifie si le token est valide pour l'utilisateur
            if(jwtService.isTokenValid(jwt, userDetails)) {
                // Crée un contexte de sécurité vide

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                // Crée un token d'authentification avec les détails de l'utilisateur
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Ajoute les détails de l'authentification au token
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Définit le contexte de sécurité avec l'authentification
                securityContext.setAuthentication(token);

                // Place le contexte de sécurité dans le SecurityContextHolder
                SecurityContextHolder.setContext(securityContext);
            }
        }

        // Après avoir vérifié le token JWT et mis à jour le contexte de sécurité si nécessaire,
        // on laisse le filtre continuer sa chaîne d'exécution.
        filterChain.doFilter(request, response);
    }
}
