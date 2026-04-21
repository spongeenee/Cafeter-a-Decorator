package com.example.demo1.database;

import com.example.demo1.models.Usuario;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends GenericDAO<Usuario> {
    public UsuarioDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(Usuario usuario) throws SQLException {
        return saveAndGetId(usuario) != null;
    }

    public Usuario saveAndGetId(Usuario usuario) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO usuarios (nombre, email, password) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.nombre());
                stmt.setString(2, usuario.email());
                stmt.setString(3, usuario.password());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return null;

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        return new Usuario(id, usuario.nombre(), usuario.email(), usuario.password(), usuario.fechaRegistro());
                    }
                }
                return null;
            } catch (SQLException e) {
                System.err.println("Error al guardar usuario: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ? WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario.nombre());
            stmt.setString(2, usuario.email());
            stmt.setString(3, usuario.password());
            stmt.setLong(4, usuario.idUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }

        return usuarios;
    }

    @Override
    protected Usuario mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id_usuario"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("fecha_registro").toLocalDateTime()
        );
    }

    @Override
    public List<Usuario> findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nombre LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuarios por nombre: " + e.getMessage());
        }
        return usuarios;
    }

    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por email: " + e.getMessage());
        }
        return null;
    }
}
