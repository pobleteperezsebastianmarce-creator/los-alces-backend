// ==========================================
// ARCHIVO: DocumentoJugadorRepository.java
// QUÉ HACE: Lista documentos por jugador_id (RF08).
// FLUJO: DocumentoJugadorService → findByJugadorId → metadata en BD.
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.DocumentoJugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoJugadorRepository extends JpaRepository<DocumentoJugador, Long> {

    List<DocumentoJugador> findByJugadorId(Long jugadorId);
}
