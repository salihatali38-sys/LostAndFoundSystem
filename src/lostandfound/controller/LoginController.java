package lostfound.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import lostfound.app.MainApp;
import lostfound.model.User;
import lostfound.service.AuthService;

import java.util.Optional;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private MainApp mainApp;
    private final AuthService authService = new AuthService();

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Please enter username and password.");
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
            mainApp.showDashboard(user);
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid username or password.");
        }
    }

    @FXML
    private void handleQuickAdminLogin() {
        // First layer: confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Admin Access");
        confirm.setHeaderText("Administrator login");
        confirm.setContentText(
                "This option is intended only for administrators.\n" +
                        "Unauthorized use is prohibited.\n\n" +
                        "Continue?"
        );

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        // Second layer: simple authorization code
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Authorization Required");
        dialog.setHeaderText("Enter admin access code");
        dialog.setContentText("Code:");

        Optional<String> codeResult = dialog.showAndWait();
        if (codeResult.isPresent()) {
            String enteredCode = codeResult.get().trim();

            // CHANGE THIS LINE TO YOUR PREFERRED CODE
            // Use something not obvious, change it before submission/demo
            String correctCode = "lfadmin2025";   // ←←← CHANGE THIS

            if (correctCode.equals(enteredCode)) {
                User admin = authService.login("admin", "admin123");
                if (admin != null) {
                    mainApp.showDashboard(admin);
                } else {
                    showAlert(Alert.AlertType.ERROR,
                            "Admin account not found or default password was changed.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Incorrect code. Access denied.");
            }
        }
    }

    @FXML
    private void handleRegister() {
        mainApp.showRegister();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}