package com.example.demo1;

import com.example.demo1.models.ExtraDecorator;
import com.example.demo1.models.ProductoFactory;
import com.example.demo1.service.ExtraService;
import com.example.demo1.service.ProductoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CafeteriaController {
    private final ProductoService productoService = new ProductoService();
    private final ExtraService extraService = new ExtraService();

    @FXML
    private Label total;
    @FXML
    private Button start;
    @FXML
    private Button restart;
    @FXML
    private Button finish;
    @FXML
    private Button removeProduct;
    @FXML
    private Button addProduct;
    @FXML
    private TextArea registro;
    @FXML
    private ComboBox<ProductoFactory> product;
    @FXML
    private ComboBox<ExtraDecorator> extra;

    @FXML
    private void initialize() {
        ObservableList<ProductoFactory> productos = FXCollections.observableList(productoService.findAll());
        product.setItems(productos);
        ObservableList<ExtraDecorator> extras = FXCollections.observableList(extraService.findAll());
        extra.setItems(extras);
    }

    @FXML
    protected void selectProduct() {
        extra.setDisable(false);
        addProduct.setDisable(false);
    }
    @FXML
    protected void addProduct() {
        registro.setText(extra.getValue().getNombre().isEmpty()
                        ? registro.getText() + "\n" + product.getValue()
                        : registro.getText() + "\n" + product.getValue() + " " + extra.getValue());
    }
    @FXML
    protected void startOrder() {
        // Order manager
        finish.setDisable(false);
        restart.setDisable(false);
        start.setDisable(true);
        // Order
        product.setDisable(false);
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
        extra.setDisable(true);
        addProduct.setDisable(true);
    }
}
