package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CafeteriaController {
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
    private ComboBox<String> product;
    @FXML
    private ComboBox<String> extra;

    @FXML
    private void initialize() {
        product.getItems().add("Pizza");
        product.getItems().add("Hamburger");
        product.getItems().add("Beef");
        product.getItems().add("Coffee");
        extra.getItems().add("-----");
        extra.getItems().add("Bacon");
        extra.getItems().add("Cheese");
        extra.getItems().add("Milk");
        extra.getItems().add("Chocolate");
    }

    @FXML
    protected void selectProduct() {
        extra.setDisable(false);
        addProduct.setDisable(false);
    }
    @FXML
    protected void addProduct() {
        registro.setText(extra.getValue().contains("-----") || extra.getValue().isEmpty()
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
