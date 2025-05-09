package com.castores.inventario.repository;

import com.castores.inventario.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    @Query("SELECT p FROM Producto p WHERE p.estatus = :estatus")
    List<Producto> findByEstatus(@Param("estatus") Integer estatus);
    
}