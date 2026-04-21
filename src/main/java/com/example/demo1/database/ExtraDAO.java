package com.example.demo1.database;

import com.example.demo1.models.ExtraDecorator;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExtraDAO extends GenericDAO<ExtraDecorator> {
    public ExtraDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(ExtraDecorator extra) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO extras (nombre, precio, activo) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, extra.getNombre());
                stmt.setDouble(2, extra.getPrecio());
                stmt.setBoolean(3, extra.getActivo());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return false;
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        extra.setID(generatedKeys.getLong(1));
                    } else {
                        return false;
                    }
                }
                return true;
            } catch (SQLException e) {
                System.err.println("Error al guardar usuario: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(ExtraDecorator extra) {
        String sql = "UPDATE productos SET nombre = ?, precio = ?, activo = ?, WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, extra.getNombre());
            stmt.setDouble(2, extra.getPrecio());
            stmt.setBoolean(3, extra.getActivo());
            stmt.setLong(4, extra.getID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar extra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar extra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ExtraDecorator findByID(Long id) {
        String sql = "SELECT * FROM extras WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<ExtraDecorator> findAll() {
        String sql = "SELECT * FROM extras";
        List<ExtraDecorator> productos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
        }

        return productos;
    }

    @Override
    protected ExtraDecorator mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new ExtraDecorator(
                rs.getString("nombre"),
                rs.getDouble("precio")
        );
    }

    @Override
    public List<ExtraDecorator> findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM extra WHERE nombre LIKE ?";
        List<ExtraDecorator> extras = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    extras.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return extras;
    }
}