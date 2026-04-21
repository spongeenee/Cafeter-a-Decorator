package com.example.demo1.controller;

import com.example.demo1.models.*;
import com.example.demo1.service.ExtraService;
import com.example.demo1.service.PedidoService;
import com.example.demo1.service.ProductoService;
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
    private long usuarioIDActual = 1;
    private Producto auxiliar;
    private ProductoFactory selected;
    private List<CrearPedidoDetalleDTO> detallesActuales = new ArrayList<>();
    private Map<Integer, List<CrearPedidoDetalleExtrasDTO>> extrasActuales = new HashMap<>();

    @FXML
    private Label productPrice;
    @FXML
    private Label total;
    @FXML
    private Label statusMessage;
    @FXML
    private Label productInOrder;
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
    private TextArea registro;
    @FXML
    private ComboBox<ProductoFactory> product;
    @FXML
    private ComboBox<ExtraDecorator> extra;
    @FXML
    private ComboBox<MetodoPago> metodoPago;

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

    @FXML
    private void initialize() {
        ObservableList<ProductoFactory> productos = FXCollections.observableList(productoService.findAll());
        product.setItems(productos);
        ObservableList<ExtraDecorator> extras = FXCollections.observableList(extraService.findAll());
        extra.setItems(extras);
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
        // UI
        detallesActuales.clear();
        extrasActuales.clear();
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
        registro.setText("REGISTRO DE ORDEN");
    }

    @FXML
    protected void restartOrder() {
        startOrder();
    }

    @FXML
    protected void finishOrder() throws SQLException {
        // Detalles del Pedido
        pedidoService.crearPedidoCompleto(usuarioIDActual, detallesActuales, extrasActuales);
        // UI
        restart.setDisable(true);
        start.setDisable(false);
        product.setDisable(true);
        statusMessage.setText("");
        updateText();
        enableExtraSelection(false);
    }

}
