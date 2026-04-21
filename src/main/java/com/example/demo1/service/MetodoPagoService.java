package com.example.demo1.service;

import com.example.demo1.database.MetodoPagoDAO;
import com.example.demo1.models.CrearMetodoPagoDTO;
import com.example.demo1.models.MetodoPago;

import java.sql.SQLException;
import java.util.List;

public class MetodoPagoService extends GenericService<MetodoPago> {
    private final MetodoPagoDAO metodoPagoDAO;

    public MetodoPagoService() {
        try {
            this.DAO = new MetodoPagoDAO();
            this.metodoPagoDAO = (MetodoPagoDAO) DAO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuevo método de pago
     */
    public MetodoPago crearMetodoPago(CrearMetodoPagoDTO dto) throws SQLException {
        MetodoPago metodo = new MetodoPago(0, dto.nombre());
        return metodoPagoDAO.saveAndGetId(metodo);
    }

    /**
     * Obtiene todos los métodos de pago
     */
    public List<MetodoPago> obtenerTodos() {
        try {
            return DAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene un método de pago por ID
     */
    public MetodoPago obtenerPorID(long id) {
        try {
            return DAO.findByID(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtiene métodos de pago por nombre
     */
    public List<MetodoPago> buscarPorNombre(String nombre) {
        try {
            return DAO.findByNombre(nombre);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Actualiza un método de pago
     */
    public boolean actualizarMetodoPago(MetodoPago metodoPago) {
        try {
            return DAO.update(metodoPago);
        } catch (SQLException e) {
            System.err.println("Error al actualizar método de pago: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un método de pago
     */
    public boolean eliminarMetodoPago(long id) {
        try {
            return DAO.delete(id);
        } catch (SQLException e) {
            System.err.println("Error al eliminar método de pago: " + e.getMessage());
            return false;
        }
    }
}
