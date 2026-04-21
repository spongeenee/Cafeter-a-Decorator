package com.example.demo1.controller;

import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField contrasena;
    @FXML
    private Label loginStatus;

    @FXML
    public void handleLogin(ActionEvent event) throws IOException {
        if (usuario.getText().isBlank() || contrasena.getText().isBlank()) {
            if (usuario.getText().isBlank())
                loginStatus.setText("Ingresa usuario");
            if (contrasena.getText().isBlank())
                loginStatus.setText("Ingresa contraseña");
            if (usuario.getText().isBlank() && contrasena.getText().isBlank())
                loginStatus.setText("Ingresa usuario y contraseña");
            loginStatus.setTextFill(Color.CRIMSON);
        }
        else {
            loginStatus.setText("Iniciando sesión...");
            loginStatus.setTextFill(Color.LIGHTSLATEGRAY);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            stage.setTitle("Costoso: Cafetería Service");
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();
        }
    }
}
