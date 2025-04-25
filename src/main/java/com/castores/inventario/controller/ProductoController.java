package com.castores.inventario.controller;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.model.Producto;
import com.castores.inventario.model.Usuario;
import com.castores.inventario.service.MovimientoService;
import com.castores.inventario.service.ProductoService;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private MovimientoService movimientoService;

    // Actualizar cantidad
    @PostMapping("/actualizar-cantidad/{id}")
    public String actualizarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad, HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Administrador".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }
    
        // Obtener el producto actual
        Producto producto = productoService.obtenerProductoPorId(id);
    
        // Validar que la cantidad no sea menor a la cantidad actual
        if (cantidad < producto.getCantidad()) {
            model.addAttribute("error", "No está permitido reducir la cantidad.");
            return "redirect:/inventario?error=No+esta+permitido+reducir+la+cantidad";
        }

        // Calcular la cantidad añadida
        int cantidadAñadida = cantidad - producto.getCantidad();
    
        // Actualizar la cantidad del producto
        producto.setCantidad(cantidad);
        productoService.actualizarCantidad(producto.getIdProducto(), cantidad);

        // Registrar el movimiento
        Usuario usuarioLogueado = (Usuario) usuario;
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.entrada); // Suponemos que es una entrada
        movimiento.setCantidad(cantidadAñadida); // Registrar solo la cantidad añadida
        movimiento.setUsuario(usuarioLogueado);
        movimiento.setFechaHora(LocalDateTime.now());
        movimientoService.guardarMovimiento(movimiento);


        return "redirect:/inventario";
        
    }

    // Cambiar estatus
    @PostMapping("/cambiar-estatus/{id}")
    public String cambiarEstatus(@PathVariable Integer id, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Administrador".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }
        productoService.cambiarEstatus(id);
        return "redirect:/inventario";
    }

    // Mostrar formulario para agregar un nuevo producto
    @GetMapping("/agregar")
    public String mostrarFormularioAgregar(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Administrador".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }
        model.addAttribute("producto", new Producto());
        return "agregar-producto"; // Vista para el formulario
    }

    // Guardar un nuevo producto
    @PostMapping("/guardar")
    public String guardarProducto(Producto producto, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Administrador".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }
        producto.setCantidad(0);

        // Guardar el producto
        productoService.guardarProducto(producto);

        // Registrar el movimiento
        Usuario usuarioLogueado = (Usuario) usuario;
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.entrada); // Usar el valor del enum
        movimiento.setCantidad(0);
        movimiento.setUsuario(usuarioLogueado);
        movimiento.setFechaHora(LocalDateTime.now());
        movimientoService.guardarMovimiento(movimiento);

        return "redirect:/inventario";
    }


    // Mostrar página de salida de productos
    @GetMapping("/salida")
    public String mostrarSalidaProductos(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Almacenista".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }

        // Obtener solo los productos activos
        model.addAttribute("productosActivos", productoService.obtenerProductosActivos());
        return "salida-productos";
    }

    // Restar cantidad de un producto
    @PostMapping("/salida/{id}")
    public String restarCantidad(@PathVariable Integer id, @RequestParam Integer cantidad, HttpSession session) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Almacenista".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }

        // Obtener el producto actual
        Producto producto = productoService.obtenerProductoPorId(id);

        // Validar que la cantidad no exceda la cantidad disponible
        if (cantidad > producto.getCantidad()) {
            return "redirect:/producto/salida?error=No puedes restar más de la cantidad disponible.";
        }

        // Restar la cantidad del producto
        int nuevaCantidad = producto.getCantidad() - cantidad;
        producto.setCantidad(nuevaCantidad);
        productoService.actualizarCantidad(producto.getIdProducto(), nuevaCantidad);

        // Registrar el movimiento
        Usuario usuarioLogueado = (Usuario) usuario;
        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(Movimiento.TipoMovimiento.salida); // Tipo: SALIDA
        movimiento.setCantidad(cantidad); // Cantidad restada
        movimiento.setUsuario(usuarioLogueado);
        movimiento.setFechaHora(LocalDateTime.now());
        movimientoService.guardarMovimiento(movimiento);

        return "redirect:/producto/salida";
    }


    @GetMapping("/movimiento")
    public String mostrarHistorialMovimientos(@RequestParam(required = false) String tipo, HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuario");
        if (usuario == null || !"Administrador".equals(((Usuario) usuario).getRol().getNombre())) {
            return "redirect:/inventario?error=No autorizado";
        }

        // Obtener movimientos según el filtro
        List<Movimiento> movimientos;
        if ("entrada".equalsIgnoreCase(tipo)) {
            movimientos = movimientoService.obtenerMovimientosPorTipo(Movimiento.TipoMovimiento.entrada);
        } else if ("salida".equalsIgnoreCase(tipo)) {
            movimientos = movimientoService.obtenerMovimientosPorTipo(Movimiento.TipoMovimiento.salida);
        } else {
            movimientos = movimientoService.obtenerTodosLosMovimientos();
        }

        // Agregar movimientos al modelo
        model.addAttribute("movimientos", movimientos);

        // Agregar el tipo seleccionado para mantener el filtro en la vista
        model.addAttribute("tipoSeleccionado", tipo);

        return "historial-movimiento"; // Vista para el historial de movimientos
    }
        

}