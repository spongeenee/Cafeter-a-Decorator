package com.example.demo1.service;

import com.example.demo1.database.PedidoDAO;
import com.example.demo1.database.PedidoDetalleDAO;
import com.example.demo1.database.PedidoDetalleExtrasDAO;
import com.example.demo1.models.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoService extends GenericService<Pedido> {
    private final PedidoDetalleDAO DetalleDAO;
    private final PedidoDetalleExtrasDAO DetalleExtraDAO;

    public PedidoService() {
        try {
            this.DAO = new PedidoDAO();
            DetalleDAO = new PedidoDetalleDAO();
            DetalleExtraDAO = new PedidoDetalleExtrasDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un pedido completo con detalles y extras
     * @param usuarioID ID del usuario
     * @param detalles Lista de detalles del pedido (sin ID)
     * @param extras Lista de extras para cada detalle: Map<indexDetalle, List<extras>>
     * @return El pedido creado con ID generado
     */
    public Pedido crearPedidoCompleto(long usuarioID, List<CrearPedidoDetalleDTO> detalles, Map<Integer, List<CrearPedidoDetalleExtrasDTO>> extras) throws SQLException {
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un detalle");
        }

        // Calcular total
        double total = calcularTotal(detalles, extras);

        // Crear pedido
        PedidoDAO pedidoDAO = (PedidoDAO) DAO;
        Pedido pedido = new Pedido(0, usuarioID, LocalDateTime.now(), total, "PENDIENTE");
        System.out.println(pedido);
        Pedido pedidoGuardado = pedidoDAO.saveAndGetId(pedido);

        if (pedidoGuardado == null) {
            throw new SQLException("Error al crear el pedido");
        }

        // Crear detalles
        for (int i = 0; i < detalles.size(); i++) {
            CrearPedidoDetalleDTO detalleDTO = detalles.get(i);
            PedidoDetalle detalle = new PedidoDetalle(0, pedidoGuardado.ID(), detalleDTO.idProducto(), detalleDTO.cantidad(), detalleDTO.precioBase());
            PedidoDetalle detalleGuardado = DetalleDAO.saveAndGetId(detalle);

            if (detalleGuardado == null) {
                throw new SQLException("Error al crear detalle del pedido");
            }

            // Crear extras para este detalle
            if (extras.containsKey(i)) {
                for (CrearPedidoDetalleExtrasDTO extraDTO : extras.get(i)) {
                    PedidoDetalleExtras extra = new PedidoDetalleExtras(0, detalleGuardado.idDetalle(), extraDTO.idExtra(), extraDTO.precioExtra());
                    PedidoDetalleExtras extraGuardado = DetalleExtraDAO.saveAndGetId(extra);

                    if (extraGuardado == null) {
                        throw new SQLException("Error al crear extra del pedido");
                    }
                }
            }
        }

        return pedidoGuardado;
    }

    /**
     * Calcula el total del pedido
     */
    private double calcularTotal(List<CrearPedidoDetalleDTO> detalles, Map<Integer, List<CrearPedidoDetalleExtrasDTO>> extras) {
        double total = 0;

        for (int i = 0; i < detalles.size(); i++) {
            CrearPedidoDetalleDTO detalle = detalles.get(i);
            total += detalle.precioBase() * detalle.cantidad();

            if (extras.containsKey(i)) {
                for (CrearPedidoDetalleExtrasDTO extra : extras.get(i)) {
                    total += extra.precioExtra();
                }
            }
        }

        return total;
    }

    /**
     * Obtiene un pedido completo con todos sus detalles y extras
     */
    public PedidoCompleto obtenerPedidoCompleto(long idPedido) throws SQLException {
        PedidoDAO pedidoDAO = (PedidoDAO) DAO;
        Pedido pedido = pedidoDAO.findByID(idPedido);
        if (pedido == null) {
            return null;
        }

        List<PedidoDetalle> detalles = DetalleDAO.findByPedidoID(idPedido);
        Map<Long, List<PedidoDetalleExtras>> extrasMap = new java.util.HashMap<>();

        for (PedidoDetalle detalle : detalles) {
            List<PedidoDetalleExtras> extrasDelDetalle = DetalleExtraDAO.findByDetalleID(detalle.idDetalle());
            extrasMap.put(detalle.idDetalle(), extrasDelDetalle);
        }

        return new PedidoCompleto(pedido, detalles, extrasMap);
    }

    /**
     * Actualiza el estado de un pedido
     */
    public boolean actualizarEstadoPedido(long idPedido, String nuevoEstado) throws SQLException {
        PedidoDAO pedidoDAO = (PedidoDAO) DAO;
        Pedido pedido = pedidoDAO.findByID(idPedido);
        
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no existe");
        }

        // Crear nuevo pedido con estado actualizado
        Pedido pedidoActualizado = new Pedido(
                pedido.ID(),
                pedido.usuarioID(),
                pedido.fecha(),
                pedido.total(),
                nuevoEstado
        );

        return pedidoDAO.update(pedidoActualizado);
    }

    /**
     * Marca un pedido como pagado
     */
    public boolean marcarPedidoPagado(long idPedido) throws SQLException {
        return actualizarEstadoPedido(idPedido, "PAGADO");
    }

    /**
     * Obtiene un pedido por ID
     */
    public Pedido obtenerPorID(long id) {
        try {
            return DAO.findByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene todos los pedidos de un usuario
     */
    public List<Pedido> obtenerPedidosPorUsuario(long usuarioID) {
        try {
            List<Pedido> todos = DAO.findAll();
            return todos.stream()
                    .filter(p -> p.usuarioID() == usuarioID)
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene el ID de todos los pedidos de un usuario
     */
    public List<Long> obtenerIDsPedidosPorUsuario(long usuarioID) {
        return obtenerPedidosPorUsuario(usuarioID)
                .stream()
                .map(Pedido::ID)
                .toList();
    }

    /**
     * Obtiene todos los pedidos con estado específico
     */
    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        try {
            List<Pedido> todos = DAO.findAll();
            return todos.stream()
                    .filter(p -> p.estado().equals(estado))
                    .toList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean register(ArrayList<ProductoFactory> productos, ArrayList<ExtraDecorator> extras, double total, boolean estado) {
        return false;
    }

    public List<Pedido> findAll() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DTO para retornar un pedido completo con detalles y extras
     */
    public record PedidoCompleto(Pedido pedido, List<PedidoDetalle> detalles, Map<Long, List<PedidoDetalleExtras>> extras) {}
}
