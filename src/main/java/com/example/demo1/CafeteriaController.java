package com.example.demo1;

import com.example.demo1.decorator.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CafeteriaController {
    private BebidaInterface bebida;
    @FXML
    private Label bebidaOrder;
    @FXML
    private Label total;
    @FXML
    private Button baseTea;
    @FXML
    private Button baseYogurt;
    @FXML
    private Button addFruta;
    @FXML
    private Button addGelatina;
    @FXML
    private Button addExtra;
    @FXML
    private Button addPop;
    @FXML
    private Button start;
    @FXML
    private Button restart;
    @FXML
    private Button finish;

    @FXML
    protected void setTeaBase() {
        baseTea.setDisable(true);
        baseYogurt.setDisable(true);
        total.setText(String.valueOf(bebida.total()));
        enableOptions(true);
    }
    @FXML
    protected void addYogurt() {
        bebida = new DecoratorYogurt(bebida);
        setTeaBase();
    }
    @FXML
    protected void addFruta() {
        bebida = new DecoratorFruta(bebida);
        total.setText(String.valueOf(bebida.total()));
    }
    @FXML
    protected void addGelatina() {
        bebida = new DecoratorGelatina(bebida);
        total.setText(String.valueOf(bebida.total()));
    }
    @FXML
    protected void addExtra() {
        bebida = new DecoratorExtra(bebida);
        total.setText(String.valueOf(bebida.total()));
    }
    @FXML
    protected void addPop() {
        bebida = new DecoratorPop(bebida);
        total.setText(String.valueOf(bebida.total()));
    }

    private void enableOptions(boolean option) {
        addFruta.setDisable(!option);
        addGelatina.setDisable(!option);
        addExtra.setDisable(!option);
        addPop.setDisable(!option);
    }
    @FXML
    protected void startOrder() {
        baseTea.setDisable(false);
        baseYogurt.setDisable(false);
        finish.setDisable(false);
        restart.setDisable(false);
        start.setDisable(true);
        bebida = new Bebida();
        total.setText(String.valueOf(bebida.total()));
    }
    @FXML
    protected void restartOrder() {
        bebida = null;
        startOrder();
    }
    @FXML
    protected void finishOrder() {
        enableOptions(false);
        restart.setDisable(true);
        start.setDisable(false);
        bebidaOrder.setText(bebida.servir());
    }
}
