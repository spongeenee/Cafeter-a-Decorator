package com.example.demo1.service;

import com.example.demo1.database.UsuarioDAO;
import com.example.demo1.models.CrearUsuarioDTO;
import com.example.demo1.models.Usuario;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioService extends GenericService<Usuario> {
    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        try {
            this.DAO = new UsuarioDAO();
            this.usuarioDAO = (UsuarioDAO) DAO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuevo usuario
     */
    public Usuario crearUsuario(CrearUsuarioDTO dto) throws SQLException {
        Usuario usuario = new Usuario(0, dto.nombre(), dto.email(), dto.password(), LocalDateTime.now());
        return usuarioDAO.saveAndGetId(usuario);
    }

    /**
     * Obtiene un usuario por email
     */
    public Usuario obtenerPorEmail(String email) throws SQLException {
        return usuarioDAO.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios
     */
    public List<Usuario> obtenerTodos() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene un usuario por ID
     */
    public Usuario obtenerPorID(long id) {
        try {
            return DAO.findByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene usuarios por nombre (búsqueda parcial)
     */
    public List<Usuario> buscarPorNombre(String nombre) {
        try {
            return DAO.findByNombre(nombre);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza un usuario
     */
    public boolean actualizarUsuario(Usuario usuario) {
        try {
            return DAO.update(usuario);
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario
     */
    public boolean eliminarUsuario(long id) {
        try {
            return DAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
