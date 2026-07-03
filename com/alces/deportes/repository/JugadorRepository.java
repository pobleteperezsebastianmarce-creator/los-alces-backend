// ==========================================
// ARCHIVO: JugadorRepository.java
// QUÉ HACE: Consultas RF01/RF09; LIKE por nombre, rut o nombre de categoría.
// FLUJO: JugadorService → JPQL personalizado → lista Jugador al Controller.
// ==========================================
package com.alces.deportes.repository;

import com.alces.deportes.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    List<Jugador> findByCategoriaId(Long categoriaId);

    boolean existsByRut(String rut);

    boolean existsByRutAndIdNot(String rut, Long id);

    @Query("""
            SELECT j FROM Jugador j JOIN j.categoria c
            WHERE LOWER(j.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
               OR LOWER(j.rut) LIKE LOWER(CONCAT('%', :termino, '%'))
               OR LOWER(c.nombreCategoria) LIKE LOWER(CONCAT('%', :termino, '%'))
            """)
    List<Jugador> buscarPorNombreRutOCategoria(@Param("termino") String termino);

    @Query("""
            SELECT j FROM Jugador j JOIN j.categoria c
            WHERE c.id = :categoriaId
              AND (LOWER(j.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
                OR LOWER(j.rut) LIKE LOWER(CONCAT('%', :termino, '%')))
            """)
    List<Jugador> buscarPorCategoriaYTermino(@Param("categoriaId") Long categoriaId,
                                             @Param("termino") String termino);
}
