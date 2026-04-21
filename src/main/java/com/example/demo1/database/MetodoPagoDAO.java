package com.example.demo1.database;

import com.example.demo1.models.MetodoPago;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagoDAO extends GenericDAO<MetodoPago> {
    public MetodoPagoDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(MetodoPago metodoPago) throws SQLException {
        return saveAndGetId(metodoPago) != null;
    }

    public MetodoPago saveAndGetId(MetodoPago metodoPago) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO metodos_pago (nombre) VALUES (?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, metodoPago.nombre());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return null;

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        return new MetodoPago(id, metodoPago.nombre());
                    }
                }
                return null;
            } catch (SQLException e) {
                System.err.println("Error al guardar método de pago: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(MetodoPago metodoPago) throws SQLException {
        String sql = "UPDATE metodos_pago SET nombre = ? WHERE id_metodo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, metodoPago.nombre());
            stmt.setLong(2, metodoPago.idMetodo());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar método de pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM metodos_pago WHERE id_metodo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar método de pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public MetodoPago findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM metodos_pago WHERE id_metodo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar método de pago por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<MetodoPago> findAll() throws SQLException {
        String sql = "SELECT * FROM metodos_pago";
        List<MetodoPago> metodos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                metodos.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los métodos de pago: " + e.getMessage());
        }

        return metodos;
    }

    @Override
    protected MetodoPago mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new MetodoPago(
                rs.getLong("id_metodo"),
                rs.getString("nombre")
        );
    }

    @Override
    public List<MetodoPago> findByNombre(String nombre) throws SQLException {
        String sql = "SELECT * FROM metodos_pago WHERE nombre LIKE ?";
        List<MetodoPago> metodos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    metodos.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar métodos de pago por nombre: " + e.getMessage());
        }
        return metodos;
    }
}
