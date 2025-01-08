package com.myproject.common;

import com.myproject.ui.dialogs.ErrorDialog;

public class ErrorHandler {
    public static void showError(String title, String message) {
        ErrorDialog.showError(title, message);
        LoggerUtil.error(title + ": " + message);
    }
}
