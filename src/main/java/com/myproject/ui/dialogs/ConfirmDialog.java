package com.myproject.ui.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ConfirmDialog {
    public static boolean showConfirm(String title, String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        a.setTitle(title);
        a.setHeaderText(null);
        return a.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
