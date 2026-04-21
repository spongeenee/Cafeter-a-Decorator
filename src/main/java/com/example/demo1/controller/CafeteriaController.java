package com.example.demo1.controller;

import com.example.demo1.models.*;
import com.example.demo1.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CafeteriaController {
    private final ProductoService productoService = new ProductoService();
    private final ExtraService extraService = new ExtraService();
    private final PedidoService pedidoService = new PedidoService();
    private final MetodoPagoService metodoPagoService = new MetodoPagoService();
    private final PagoService pagoService = new PagoService();
    private long usuarioIDActual;
    private Producto auxiliar;
    private ProductoFactory selected;
    private Pedido pedidoInProcess;
    private final List<CrearPedidoDetalleDTO> detallesActuales = new ArrayList<>();
    private final Map<Integer, List<CrearPedidoDetalleExtrasDTO>> extrasActuales = new HashMap<>();

    @FXML
    private Label productPrice;
    @FXML
    private Label total;
    @FXML
    private Label statusMessage;
    @FXML
    private Label productInOrder;
    @FXML
    private Label pagoLabel;
    @FXML
    private Label paymentStatus;
    @FXML
    private Button start;
    @FXML
    private Button restart;
    @FXML
    private Button finish;
    @FXML
    private Button addExtra;
    @FXML
    private Button addProduct;
    @FXML
    private Button pay;
    @FXML
    private TextArea registro;
    @FXML
    private TextField monto;
    @FXML
    private ComboBox<ProductoFactory> product;
    @FXML
    private ComboBox<ExtraDecorator> extra;
    @FXML
    private ComboBox<MetodoPago> metodoPago;

    public void setUsuarioIDActual(long ID) {
        this.usuarioIDActual = ID;
    }

    /**
     * Auxiliar para mantener el registro de los extras
     */
    private void updateProduct() {
        auxiliar = new ExtraDecorator(auxiliar, extra.getValue());
        updateText();
    }

    private void updateText() {
        productInOrder.setText(auxiliar.nombre());
        productPrice.setText(String.valueOf(auxiliar.precio()));
    }

    /**
     * Auxiliar para activar la ComboBox y botones relacionados con extras
     * @param option true para habilitarlos, false para deshabilitarlos
     */
    private void enableExtraSelection(boolean option) {
        extra.setDisable(!option);
        addProduct.setDisable(!option);
        addExtra.setDisable(!option);
    }

    private void enablePayment(boolean option) {
        pagoLabel.setDisable(!option);
        metodoPago.setDisable(!option);
        pay.setDisable(!option);
    }

    @FXML
    private void initialize() {
        ObservableList<ProductoFactory> productos = FXCollections.observableList(productoService.findAll());
        product.setItems(productos);
        ObservableList<ExtraDecorator> extras = FXCollections.observableList(extraService.findAll());
        extra.setItems(extras);
        ObservableList<MetodoPago> metodoPagos = FXCollections.observableList(metodoPagoService.obtenerTodos());
        metodoPago.setItems(metodoPagos);
    }

    @FXML
    protected void selectProduct() {
        enableExtraSelection(true);
        auxiliar = product.getValue();
        selected = product.getValue();
        updateText();
    }

    @FXML
    protected void addProduct() {
        // CODE
        detallesActuales.add(new CrearPedidoDetalleDTO(product.getValue().ID(), 1, selected.precio()));
        System.out.println(detallesActuales);
        // UI
        registro.setText(registro.getText() + "\n" + auxiliar.nombre());
        updateText();
        total.setText(String.valueOf(Double.parseDouble(total.getText()) + auxiliar.precio()));
        statusMessage.setDisable(false);
        statusMessage.setText("Producto agregado exitosamente:\n" + auxiliar.nombre());
        statusMessage.setTextFill(Color.GREEN);
        auxiliar = product.getValue();
    }

    @FXML
    protected void addExtra() {
        if (extra.getValue() != null) {
            // CODE
            int productIndex = detallesActuales.size() - 1;
            extrasActuales.
                    computeIfAbsent(productIndex,k -> new ArrayList<>()).
                    add(new CrearPedidoDetalleExtrasDTO(extra.getValue().getID(), extra.getValue().getPrecio()));
            System.out.println(extrasActuales);
            // UI
            updateProduct();
            statusMessage.setDisable(false);
            statusMessage.setText("Complemento agregado: " + extra.getValue());
            statusMessage.setTextFill(Color.LIGHTSLATEGRAY);
        } else {
            statusMessage.setDisable(false);
            statusMessage.setText("Especifica un extra para agregar");
            statusMessage.setTextFill(Color.CRIMSON);
        }
    }

    @FXML
    protected void startOrder() {
        // CODE
        detallesActuales.clear();
        extrasActuales.clear();
        // UI
        // Order manager
        finish.setDisable(false);
        restart.setDisable(false);
        start.setDisable(true);
        // Order
        product.setDisable(false);
        if (product.getValue() != null) enableExtraSelection(true);
        productInOrder.setText("");
        statusMessage.setText("");
        total.setText("0.00");
        registro.setText("===REGISTRO DE ORDEN===");
        enablePayment(false);
    }

    @FXML
    protected void restartOrder() {
        startOrder();
    }

    @FXML
    protected void finishOrder() {
        // Detalles del Pedido
        try {
            pedidoInProcess = pedidoService.crearPedidoCompleto(usuarioIDActual, detallesActuales, extrasActuales);
        } catch (SQLException e) {
            statusMessage.setText(e.getMessage());
            statusMessage.setTextFill(Color.CRIMSON);
        }
        // UI
        restart.setDisable(true);
        start.setDisable(false);
        product.setDisable(true);
        statusMessage.setText("");
        monto.setPromptText(total.getText());
        updateText();
        enableExtraSelection(false);
        enablePayment(true);
    }

    @FXML
    protected void payment() {
        if (!monto.getText().isBlank()) {
            double monto = Double.parseDouble(this.monto.getText());
            double total = Double.parseDouble(this.total.getText());
            if (monto != total) {
                paymentStatus.setText("Pago insuficiente");
                paymentStatus.setTextFill(Color.CRIMSON);
            } else if (monto == total) {
                paymentStatus.setText("Pago exitoso");
                paymentStatus.setTextFill(Color.GREEN);
                long ID = pedidoInProcess.ID();
                try {
                    pedidoService.marcarPedidoPagado(ID);
                    pagoService.registrarPago(new CrearPagoDTO(ID, metodoPago.getValue().idMetodo(), monto, ("Registrado por usuario " + usuarioIDActual)));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            paymentStatus.setText("Ingresar cantidad a pagar");
            paymentStatus.setTextFill(Color.CRIMSON);
        }
    }
}
