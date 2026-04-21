package com.example.demo1.database;

import com.example.demo1.models.PedidoDetalleExtras;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDetalleExtrasDAO extends GenericDAO<PedidoDetalleExtras> {
    public PedidoDetalleExtrasDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(PedidoDetalleExtras detalleExtras) throws SQLException {
        return saveAndGetId(detalleExtras) != null;
    }

    public PedidoDetalleExtras saveAndGetId(PedidoDetalleExtras detalleExtras) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO pedido_detalle_extras (id_detalle, id_extra, precio_extra) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, detalleExtras.idDetalle());
                stmt.setLong(2, detalleExtras.idExtra());
                stmt.setDouble(3, detalleExtras.precioExtra());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return null;

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        return new PedidoDetalleExtras(id, detalleExtras.idDetalle(), detalleExtras.idExtra(), detalleExtras.precioExtra());
                    }
                }
                return null;
            } catch (SQLException e) {
                System.err.println("Error al guardar pedido detalle extra: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(PedidoDetalleExtras detalleExtras) throws SQLException {
        String sql = "UPDATE pedido_detalle_extras SET id_detalle = ?, id_extra = ?, precio_extra = ? WHERE id_detalle_extra = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, detalleExtras.idDetalle());
            stmt.setLong(2, detalleExtras.idExtra());
            stmt.setDouble(3, detalleExtras.precioExtra());
            stmt.setLong(4, detalleExtras.idDetalleExtra());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido detalle extra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM pedido_detalle_extras WHERE id_detalle_extra = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido detalle extra: " + e.getMessage());
            return false;
        }
    }

    @Override
    public PedidoDetalleExtras findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM pedido_detalle_extras WHERE id_detalle_extra = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido detalle extra por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<PedidoDetalleExtras> findAll() throws SQLException {
        String sql = "SELECT * FROM pedido_detalle_extras";
        List<PedidoDetalleExtras> extras = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                extras.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pedidos detalles extras: " + e.getMessage());
        }

        return extras;
    }

    @Override
    protected PedidoDetalleExtras mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new PedidoDetalleExtras(
                rs.getLong("id_detalle_extra"),
                rs.getLong("id_detalle"),
                rs.getLong("id_extra"),
                rs.getDouble("precio_extra")
        );
    }

    @Override
    public List<PedidoDetalleExtras> findByNombre(String nombre) throws SQLException {
        return List.of();
    }

    public List<PedidoDetalleExtras> findByDetalleID(Long idDetalle) throws SQLException {
        String sql = "SELECT * FROM pedido_detalle_extras WHERE id_detalle = ?";
        List<PedidoDetalleExtras> extras = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idDetalle);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    extras.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar extras por ID de detalle: " + e.getMessage());
        }

        return extras;
    }
}
