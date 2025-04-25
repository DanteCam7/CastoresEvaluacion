package com.castores.inventario.service;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.repository.MovimientoRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    public void guardarMovimiento(Movimiento movimiento) {
        movimientoRepository.save(movimiento);
    }

    public List<Movimiento> obtenerTodosLosMovimientos() {
        return movimientoRepository.findAllWithJoins();
    }

    public List<Movimiento> obtenerMovimientosPorTipo(Movimiento.TipoMovimiento tipo) {
        return movimientoRepository.findByTipoWithJoins(tipo);
    }
}