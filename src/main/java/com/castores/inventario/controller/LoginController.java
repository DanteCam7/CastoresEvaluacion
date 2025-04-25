package com.castores.inventario.controller;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    // Mostrar la página de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    // Procesar el inicio de sesión
    @PostMapping("/login")
    public String iniciarSesion(
            @RequestParam("correo") String correo,
            @RequestParam("contrasena") String contrasena,
            HttpSession session,
            Model model) {

        // Buscar al usuario en la base de datos
        Usuario usuario = usuarioService.findByCorreo(correo);

        if (usuario != null && usuario.getContrasena().equals(contrasena)) {
            // Autenticación exitosa
            session.setAttribute("usuario", usuario); // Guardar el usuario en la sesión

            // Redirigir según el rol
            if ("Administrador".equals(usuario.getRol().getNombre())) {
                return "redirect:/inventario";
            } else if ("Almacenista".equals(usuario.getRol().getNombre())) {
                return "redirect:/inventario";
            }
        }

        // Autenticación fallida
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }

    // Cerrar sesión
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login";
    }
}