package com.example.demo1;

import com.example.demo1.models.ExtraDecorator;
import com.example.demo1.models.Producto;
import com.example.demo1.models.ProductoFactory;
import com.example.demo1.service.ExtraService;
import com.example.demo1.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

public class CafeteriaController {
    private final ProductoService productoService = new ProductoService();
    private final ExtraService extraService = new ExtraService();
    private Producto auxiliar;

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
        updateText();
    }

    @FXML
    protected void addProduct() {
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
    protected void finishOrder() {
        restart.setDisable(true);
        start.setDisable(false);
        product.setDisable(true);
        statusMessage.setText("");
        updateText();
        enableExtraSelection(false);
    }
}
