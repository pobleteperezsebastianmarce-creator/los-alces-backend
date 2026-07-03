// ==========================================
// ARCHIVO: CategoriaRepository.java
// QUÉ HACE: JPA básico sobre categoria_deportiva.
// FLUJO: CategoriaService → findAll/save → alimenta combos del front.
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.CategoriaDeportiva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<CategoriaDeportiva, Long> {
}
