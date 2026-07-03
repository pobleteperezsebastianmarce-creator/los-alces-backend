// ==========================================
// ARCHIVO: UsuarioRepository.java
// QUÉ HACE: Capa datos login; busca por username en tabla usuario.
// FLUJO: AuthService / Sha256Provider → JPA → MySQL usuario (password_hash).
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);
}
