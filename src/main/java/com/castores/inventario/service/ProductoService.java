package com.castores.inventario.service;

import com.castores.inventario.model.Producto;
import com.castores.inventario.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void guardarProducto(Producto producto) {
        productoRepository.save(producto);
    }

    public void actualizarCantidad(Integer id, Integer cantidad) {
        Producto producto = obtenerProductoPorId(id);
        producto.setCantidad(cantidad);
        productoRepository.save(producto);
    }

    public void cambiarEstatus(Integer id) {
        Producto producto = obtenerProductoPorId(id);
        producto.setEstatus(producto.getEstatus() == 1 ? 0 : 1); // Cambiar entre Activo e Inactivo
        productoRepository.save(producto);
    }

    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByEstatus(1); // Filtrar por estatus activo
    }
}