package com.example.demo1.database;

import com.example.demo1.models.Pago;
import com.example.demo1.singleton.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO extends GenericDAO<Pago> {
    public PagoDAO() throws SQLException {
        super();
    }

    @Override
    public boolean save(Pago pago) throws SQLException {
        return saveAndGetId(pago) != null;
    }

    public Pago saveAndGetId(Pago pago) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO pagos (id_pedido, id_metodo, monto, referencia) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, pago.idPedido());
                stmt.setLong(2, pago.idMetodo());
                stmt.setDouble(3, pago.monto());
                stmt.setString(4, pago.referencia());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) return null;

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        return new Pago(id, pago.idPedido(), pago.idMetodo(), pago.monto(), pago.fecha(), pago.referencia());
                    }
                }
                return null;
            } catch (SQLException e) {
                System.err.println("Error al guardar pago: " + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Pago pago) throws SQLException {
        String sql = "UPDATE pagos SET id_pedido = ?, id_metodo = ?, monto = ?, referencia = ? WHERE id_pago = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, pago.idPedido());
            stmt.setLong(2, pago.idMetodo());
            stmt.setDouble(3, pago.monto());
            stmt.setString(4, pago.referencia());
            stmt.setLong(5, pago.idPago());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long ID) throws SQLException {
        String sql = "DELETE FROM pagos WHERE id_pago = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Pago findByID(Long ID) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id_pago = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pago por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Pago> findAll() throws SQLException {
        String sql = "SELECT * FROM pagos";
        List<Pago> pagos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pagos.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los pagos: " + e.getMessage());
        }

        return pagos;
    }

    @Override
    protected Pago mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Pago(
                rs.getLong("id_pago"),
                rs.getLong("id_pedido"),
                rs.getLong("id_metodo"),
                rs.getDouble("monto"),
                rs.getTimestamp("fecha").toLocalDateTime(),
                rs.getString("referencia")
        );
    }

    @Override
    public List<Pago> findByNombre(String nombre) throws SQLException {
        return List.of();
    }

    public List<Pago> findByPedidoID(Long idPedido) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id_pedido = ?";
        List<Pago> pagos = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, idPedido);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pagos.add(mapResultSetToEntity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pagos por ID de pedido: " + e.getMessage());
        }

        return pagos;
    }
}
