package com.example.demo1.controller;

import com.example.demo1.HelloApplication;
import com.example.demo1.models.Usuario;
import com.example.demo1.service.UsuarioService;
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
import java.sql.SQLException;

public class LoginController {
    private final UsuarioService service = new UsuarioService();
    @FXML
    private TextField usuario;
    @FXML
    private PasswordField contrasena;
    @FXML
    private Label loginStatus;

    @FXML
    public void handleLogin(ActionEvent event) throws IOException, SQLException {
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
            Usuario auxiliar = service.obtenerPorEmail(usuario.getText());

            if (auxiliar == null) {
                loginStatus.setText("Usuario no encontrado");
                loginStatus.setTextFill(Color.CRIMSON);
            }
            else {
                if (!auxiliar.password().equals(contrasena.getText())) {
                    loginStatus.setText("Contraseña incorrecta");
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
    }
}
