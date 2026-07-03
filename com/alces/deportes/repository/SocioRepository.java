// ==========================================
// ARCHIVO: SocioRepository.java
// QUÉ HACE: Consultas RF02/RF09; findAll y buscarPorNombreORut (LIKE).
// FLUJO: SocioService → SQL/JPQL → devuelve entidades Socio al Service.
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.Socio;
import com.alces.deportes.model.enums.EstadoMembresia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    boolean existsByRut(String rut);

    boolean existsByRutAndIdNot(String rut, Long id);

    List<Socio> findByEstadoMembresia(EstadoMembresia estadoMembresia);

    @Query("""
            SELECT s FROM Socio s
            WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
               OR LOWER(s.rut) LIKE LOWER(CONCAT('%', :termino, '%'))
            """)
    List<Socio> buscarPorNombreORut(@Param("termino") String termino);
}
