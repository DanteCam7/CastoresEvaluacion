package com.castores.inventario.service;

import com.castores.inventario.model.Usuario;
import com.castores.inventario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario findByCorreo(String correo) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(correo);
        return optionalUsuario.orElse(null);
    }
}