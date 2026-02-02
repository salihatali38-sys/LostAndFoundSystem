package lostfound.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lostfound.app.MainApp;
import lostfound.service.AuthService;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private MainApp mainApp;
    private final AuthService authService = new AuthService();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        String result = authService.register(username, password, "USER");  // always USER
        if ("SUCCESS".equals(result)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!\nYou can now log in.");
            if (mainApp != null) {
                mainApp.showLogin();
            }
            usernameField.getScene().getWindow().hide();
        } else {
            showAlert(Alert.AlertType.ERROR, result);
        }
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}