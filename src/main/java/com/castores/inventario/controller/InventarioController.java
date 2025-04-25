package com.castores.inventario.controller;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventarioController {

    @Autowired // Inyectar ProductoService
    private ProductoService productoService;

    @GetMapping("/inventario")
    public String inventario(HttpSession session, Model model) {
        // Verificar si el usuario está autenticado
        Object usuario = session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Pasar el rol del usuario a la vista
        model.addAttribute("rol", ((Usuario) usuario).getRol().getNombre());

        // Obtener la lista de productos desde el servicio
        model.addAttribute("productos", productoService.obtenerTodosLosProductos());
        return "inventario";
    }
}