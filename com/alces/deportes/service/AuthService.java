// ==========================================
// ARCHIVO: AuthService.java
// QUÉ HACE: Lógica de sesión RF05; orquesta login contra Spring Security.
// FLUJO: AuthController → AuthenticationManager/Provider → UsuarioRepository.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.dto.auth.LoginRequestDTO;
import com.alces.deportes.dto.auth.LoginResponseDTO;
import com.alces.deportes.model.Usuario;
import com.alces.deportes.repository.UsuarioRepository;
import com.alces.deportes.util.Sha256Util;
import com.alces.deportes.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

// Valida usuario contra tabla usuario; la clave va hasheada SHA-256 (RNF01)
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;

    // Compara password con password_hash de MySQL vía Sha256Util
    public LoginResponseDTO login(LoginRequestDTO request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        return new LoginResponseDTO(
                usuario.getUsername(),
                usuario.getNombre(),
                usuario.getRol(),
                "Autenticación exitosa con SHA-256");
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public LoginResponseDTO usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BusinessException("No hay sesión activa");
        }
        Usuario usuario = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));
        return new LoginResponseDTO(usuario.getUsername(), usuario.getNombre(), usuario.getRol(), "Sesión activa");
    }
}
