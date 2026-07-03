// ==========================================
// ARCHIVO: SecurityConfig.java
// QUÉ HACE: Security RF06; define qué rol puede llamar cada ruta (/api/pagos, /api/jugadores, etc.).
// FLUJO: Cada petición HTTP pasa por acá antes del Controller; sin permiso → 401/403.
// ==========================================
package com.alces.deportes.config;

import com.alces.deportes.security.Sha256AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

// RF06 - quién puede pegarle a cada URL (roles del diagrama de casos de uso)
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String ROLE_ADMINISTRADOR = "ADMINISTRADOR";
    private static final String ROLE_TESORERO = "TESORERO";
    private static final String ROLE_ENTRENADOR = "ENTRENADOR";
    private static final String ROLE_SOCIO = "SOCIO";
    private static final String API_SOCIOS_ALL = "/api/socios/**";

    private final Sha256AuthenticationProvider sha256AuthenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(sha256AuthenticationProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/css/**", "/js/**", "/img/**", "/archivos/**", "/favicon.ico")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers("/api/reportes/**", "/api/pagos/**", "/api/alertas/**", "/api/respaldo/**")
                        .hasAnyRole(ROLE_ADMINISTRADOR, ROLE_TESORERO)
                        .requestMatchers(HttpMethod.POST, "/api/socios", API_SOCIOS_ALL).hasAnyRole(ROLE_ADMINISTRADOR, ROLE_TESORERO)
                        .requestMatchers(HttpMethod.PUT, API_SOCIOS_ALL).hasAnyRole(ROLE_ADMINISTRADOR, ROLE_TESORERO)
                        .requestMatchers(HttpMethod.DELETE, API_SOCIOS_ALL).hasAnyRole(ROLE_ADMINISTRADOR, ROLE_TESORERO)
                        .requestMatchers("/api/jugadores/**", "/api/categorias/**", "/api/documentos/**")
                        .hasAnyRole(ROLE_ADMINISTRADOR, ROLE_ENTRENADOR)
                        .requestMatchers(HttpMethod.GET, "/api/socios", API_SOCIOS_ALL)
                        .hasAnyRole(ROLE_ADMINISTRADOR, ROLE_TESORERO, ROLE_ENTRENADOR, ROLE_SOCIO)
                        .requestMatchers("/api/auth/**").authenticated()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return http.build();
    }
}
