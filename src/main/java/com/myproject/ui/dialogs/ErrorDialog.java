package com.myproject.ui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorDialog {
    public static void showError(String title, String message) {
        Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
