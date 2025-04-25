package com.castores.inventario.repository;

import com.castores.inventario.model.Movimiento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findByTipo(Movimiento.TipoMovimiento tipo);

    @Query("SELECT m FROM Movimiento m JOIN FETCH m.producto JOIN FETCH m.usuario ORDER BY m.fechaHora DESC")
    List<Movimiento> findAllWithJoins();

    @Query("SELECT m FROM Movimiento m JOIN FETCH m.producto JOIN FETCH m.usuario WHERE m.tipo = :tipo ORDER BY m.fechaHora DESC")
    List<Movimiento> findByTipoWithJoins(@Param("tipo") Movimiento.TipoMovimiento tipo);

}