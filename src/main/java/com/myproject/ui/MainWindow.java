package com.myproject.ui;

import com.myproject.common.LoggerUtil;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow {

    private GuiController controller;
    private Scene scene;

    public MainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
            BorderPane root = loader.load();
            controller = loader.getController();
            scene = new Scene(root, 800, 600);
            // Можно добавить стили при желании
            // scene.getStylesheets().add(getClass().getResource("/ui/Styles/light-theme.css").toExternalForm());
        } catch (IOException e) {
            LoggerUtil.error("Failed to load main_window.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void show(Stage stage) {
        stage.setTitle("Simple3DViewer Enhanced");
        stage.setScene(scene);
        stage.show();
    }

    public GuiController getController() {
        return controller;
    }
}
