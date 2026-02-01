package lostfound.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lostfound.controller.AdminDashboardController;
import lostfound.controller.DashboardController;
import lostfound.controller.LoginController;
import lostfound.controller.RegisterController;
import lostfound.model.User;
import lostfound.util.DatabaseInitializer;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private Stage registerStage;

    @Override
    public void start(Stage stage) throws IOException {
        this.primaryStage = stage;
        DatabaseInitializer.initialize();
        showLogin();
    }

    public void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            LoginController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setTitle("Lost & Found - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            if (registerStage != null && registerStage.isShowing()) {
                registerStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRegister() {
        if (registerStage != null && registerStage.isShowing()) {
            registerStage.requestFocus();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/register.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            RegisterController controller = loader.getController();
            controller.setMainApp(this);

            registerStage = new Stage();
            registerStage.setTitle("Lost & Found - Register");
            registerStage.setScene(scene);
            registerStage.setResizable(false);
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDashboard(User user) {
        try {
            FXMLLoader loader;
            Scene scene;

            if ("ADMIN".equals(user.getRole())) {
                loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/dashboard_admin.fxml"));
                scene = new Scene(loader.load());
                AdminDashboardController controller = loader.getController();
                controller.setMainApp(this);
                controller.setCurrentUser(user);
            } else {
                loader = new FXMLLoader(getClass().getResource("/lostfound/fxml/dashboard.fxml"));
                scene = new Scene(loader.load());
                DashboardController controller = loader.getController();
                controller.setMainApp(this);
                controller.setCurrentUser(user);
            }

            primaryStage.setTitle("Lost & Found - " + user.getRole() + " Dashboard");
            primaryStage.setScene(scene);
            primaryStage.setResizable(true);
            primaryStage.show();

            if (registerStage != null && registerStage.isShowing()) {
                registerStage.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load dashboard: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
