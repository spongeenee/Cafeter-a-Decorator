package com.example.demo1.database;

import com.example.demo1.models.PedidoDetalle;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDetalleDAO extends GenericDAO<PedidoDetalle> {
    public PedidoDetalleDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(PedidoDetalle detalleDAO) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO pedido_detalle (id_pedido, id_producto, cantidad, precio_base) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, detalleDAO.idPedido());
                stmt.setLong(2, detalleDAO.idProducto());
                stmt.setInt(3, detalleDAO.cantidad());
                stmt.setDouble(4, detalleDAO.precioBase());

                int affectedRows = stmt.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Error al guardar pedido detalle: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(PedidoDetalle detalleDAO) throws SQLException {
        String sql = "UPDATE pedido_detalle SET id_pedido = ?, id_producto = ?, cantidad = ?, precio_base = ? WHERE id_detalle = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, detalleDAO.idPedido());
            stmt.setLong(2, detalleDAO.idProducto());
            stmt.setInt(3, detalleDAO.cantidad());
            stmt.setDouble(4, detalleDAO.precioBase());
            stmt.setLong(5, detalleDAO.idDetalle());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido detalle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM pedido_detalle WHERE id_detalle = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido detalle: " + e.getMessage());
            return false;
        }
    }

    @Override
    public PedidoDetalle findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM pedido_detalle WHERE id_detalle = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido detalle por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<PedidoDetalle> findAll() throws SQLException {
        String sql = "SELECT * FROM pedido_detalle";
        List<PedidoDetalle> detalles = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pedidos detalles: " + e.getMessage());
        }

        return detalles;
    }

    @Override
    protected PedidoDetalle mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new PedidoDetalle(
                rs.getLong("id_detalle"),
                rs.getLong("id_pedido"),
                rs.getLong("id_producto"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_base")
        );
    }

    @Override
    public List<PedidoDetalle> findByNombre(String nombre) throws SQLException {
        return List.of();
    }

    public List<PedidoDetalle> findByPedidoID(Long idPedido) throws SQLException {
        String sql = "SELECT * FROM pedido_detalle WHERE id_pedido = ?";
        List<PedidoDetalle> detalles = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar detalles por ID de pedido: " + e.getMessage());
        }

        return detalles;
    }
}
