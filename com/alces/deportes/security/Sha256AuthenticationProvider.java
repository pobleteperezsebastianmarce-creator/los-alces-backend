// ==========================================
// ARCHIVO: Sha256AuthenticationProvider.java
// QUÉ HACE: Security RF05; compara la clave ingresada con password_hash (SHA-256 en BD).
// FLUJO: POST /api/auth/login → hashea password → si no coincide con usuario → 401.
// ==========================================
package com.alces.deportes.security;

import com.alces.deportes.model.Usuario;
import com.alces.deportes.repository.UsuarioRepository;
import com.alces.deportes.util.Sha256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

// Acá Spring Security chequea el hash SHA-256 antes de dejar pasar al login
@Component
@RequiredArgsConstructor
public class Sha256AuthenticationProvider implements AuthenticationProvider {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!usuario.isActivo() || !Sha256Util.matches(password, usuario.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return new UsernamePasswordAuthenticationToken(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
