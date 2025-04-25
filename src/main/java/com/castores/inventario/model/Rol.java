package com.castores.inventario.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @Column(name = "id_rol", nullable = false)
    private Integer idRol;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Relación muchos a uno con Usuario
    @OneToMany(mappedBy = "rol")
    private Set<Usuario> usuarios;

    // Getters y Setters
    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}