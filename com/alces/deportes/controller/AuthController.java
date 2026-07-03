// ==========================================
// ARCHIVO: AuthController.java
// QUÉ HACE: REST (CSR) login RF05; rutas /api/auth/login, /logout y /me.
// FLUJO: JSON de app.js → AuthService → tabla usuario en MariaDB.
// ==========================================
package com.alces.deportes.controller;

import com.alces.deportes.dto.auth.LoginRequestDTO;
import com.alces.deportes.dto.auth.LoginResponseDTO;
import com.alces.deportes.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// RF05 - login y sesión. Rutas bajo /api/auth
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // POST /api/auth/login - body: username + password (el front manda JSON desde login-form)
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletRequest httpRequest) {
        return ResponseEntity.ok(authService.login(request, httpRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<MapMessage> logout(HttpServletRequest request) {
        authService.logout();
        request.getSession(false);
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        return ResponseEntity.ok(new MapMessage("Sesión cerrada correctamente"));
    }

    // GET /api/auth/me - devuelve quién está logueado (app.js lo usa al recargar la página)
    @GetMapping("/me")
    public ResponseEntity<LoginResponseDTO> usuarioActual() {
        return ResponseEntity.ok(authService.usuarioActual());
    }

    public record MapMessage(String mensaje) {
    }
}
