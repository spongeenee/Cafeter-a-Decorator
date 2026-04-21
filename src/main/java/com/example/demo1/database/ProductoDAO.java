package com.example.demo1.database;

import com.example.demo1.models.ProductoFactory;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO extends GenericDAO<ProductoFactory> {
    public ProductoDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(ProductoFactory producto) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO productos (nombre, descripcion, precio, activo) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, producto.getNombre());
                stmt.setString(2, producto.getDescripcion());
                stmt.setDouble(3, producto.getPrecio());
                stmt.setBoolean(4, producto.getActivo());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return false;
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        producto.setID(generatedKeys.getLong(1));
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
    public boolean update(ProductoFactory producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, activo = ?, WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getDescripcion());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setBoolean(4, producto.getActivo());
            stmt.setLong(5, producto.ID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
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
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ProductoFactory findByID(Long id) {
        String sql = "SELECT * FROM productos WHERE id = ?";

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
    public List<ProductoFactory> findAll() {
        String sql = "SELECT * FROM productos";
        List<ProductoFactory> productos = new ArrayList<>();

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
    protected ProductoFactory mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new ProductoFactory(
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio_base")
                );
    }

    @Override
    public List<ProductoFactory> findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";
        List<ProductoFactory> productos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos por nombre: " + e.getMessage());
        }
        return productos;
    }
}
