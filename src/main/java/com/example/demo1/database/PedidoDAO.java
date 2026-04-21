package com.example.demo1.database;

import com.example.demo1.models.Pedido;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends GenericDAO<Pedido> {
    public PedidoDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(Pedido pedido) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO pedidos (id_usuario, fecha, total, estado) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, pedido.usuarioID());
                stmt.setTimestamp(2, Timestamp.valueOf(pedido.fecha()));
                stmt.setDouble(3, pedido.total());
                stmt.setString(4, pedido.estado());

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Error al guardar pedido: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET id_usuario = ?, fecha = ?, total = ?, estado = ? WHERE id_pedido = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pedido.usuarioID());
            stmt.setTimestamp(2, Timestamp.valueOf(pedido.fecha()));
            stmt.setDouble(3, pedido.total());
            stmt.setString(4, pedido.estado());
            stmt.setLong(5, pedido.ID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id_pedido = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Pedido findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM pedidos WHERE id_pedido = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pedido> findAll() throws SQLException {
        String sql = "SELECT * FROM pedidos";
        List<Pedido> pedidos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    @Override
    protected Pedido mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Pedido(
                rs.getLong("id_pedido"),
                rs.getLong("id_usuario"),
                rs.getTimestamp("fecha").toLocalDateTime(),
                rs.getDouble("total"),
                rs.getString("estado")
        );
    }

    @Override
    public List<Pedido> findByNombre(String nombre) throws SQLException {
        return List.of();
    }
}
