// ==========================================
// ARCHIVO: UsuarioActualService.java
// QUÉ HACE: Obtiene el usuario logueado para auditoría (gestor/administrador FK).
// FLUJO: SecurityContext (sesión) → UsuarioRepository → usado en Socio/Jugador al guardar.
// ==========================================
package com.alces.deportes.service;

import com.alces.deportes.model.Usuario;
import com.alces.deportes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioActualService {

    private final UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return usuarioRepository.findByUsername(auth.getName()).orElse(null);
    }
}
