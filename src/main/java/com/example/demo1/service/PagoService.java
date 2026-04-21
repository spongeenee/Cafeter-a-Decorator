package com.example.demo1.service;

import com.example.demo1.database.PagoDAO;
import com.example.demo1.database.PedidoDAO;
import com.example.demo1.models.CrearPagoDTO;
import com.example.demo1.models.Pago;
import com.example.demo1.models.Pedido;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PagoService extends GenericService<Pago> {
    private final PagoDAO pagoDAO;
    private final PedidoDAO pedidoDAO;

    public PagoService() {
        try {
            this.DAO = new PagoDAO();
            this.pagoDAO = (PagoDAO) DAO;
            this.pedidoDAO = new PedidoDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registra un pago para un pedido
     */
    public Pago registrarPago(CrearPagoDTO dto) throws SQLException {
        // Validar que el pedido existe
        Pedido pedido = pedidoDAO.findByID(dto.idPedido());
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }

        // Validar que el monto sea positivo
        if (dto.monto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }

        Pago pago = new Pago(0, dto.idPedido(), dto.idMetodo(), dto.monto(), LocalDateTime.now(), dto.referencia());
        return pagoDAO.saveAndGetId(pago);
    }

    /**
     * Obtiene todos los pagos
     */
    public List<Pago> obtenerTodos() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene un pago por ID
     */
    public Pago obtenerPorID(long id) {
        try {
            return DAO.findByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todos los pagos de un pedido
     */
    public List<Pago> obtenerPagosPorPedido(long idPedido) {
        try {
            return pagoDAO.findByPedidoID(idPedido);
        } catch (SQLException e) {
            System.err.println("Error al obtener pagos del pedido: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Calcula el total pagado de un pedido
     */
    public double calcularTotalPagado(long idPedido) {
        List<Pago> pagos = obtenerPagosPorPedido(idPedido);
        return pagos.stream().mapToDouble(Pago::monto).sum();
    }

    /**
     * Verifica si un pedido está completamente pagado
     */
    public boolean pedidoEstaCompleto(long idPedido) throws SQLException {
        Pedido pedido = pedidoDAO.findByID(idPedido);
        if (pedido == null) return false;

        double totalPagado = calcularTotalPagado(idPedido);
        return totalPagado >= pedido.total();
    }

    /**
     * Actualiza un pago
     */
    public boolean actualizarPago(Pago pago) {
        try {
            return DAO.update(pago);
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un pago
     */
    public boolean eliminarPago(long id) {
        try {
            return DAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar pago: " + e.getMessage());
            return false;
        }
    }
}
