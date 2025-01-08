package com.myproject.ui;

import com.myproject.common.ErrorHandler;
import com.myproject.common.LoggerUtil;
import com.myproject.io.ObjReader;
import com.myproject.io.ObjWriter;
import com.myproject.model.Model;
import com.myproject.render_engine.RenderEngine;
import com.myproject.scene.SceneManager;
import com.myproject.scene.SceneModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.scene.canvas.Canvas;

import java.io.File;

/**
 * Контроллер для меню приложения.
 * Обрабатывает действия меню "Open" и "Save".
 */
public class MenuBarController {

    private SceneManager sceneManager;
    private RenderEngine renderEngine;
    private Canvas canvas;

    /**
     * Конструктор принимает SceneManager, RenderEngine и Canvas.
     *
     * @param sceneManager  менеджер сцены с моделями
     * @param renderEngine  движок рендеринга
     * @param canvas        Canvas для отрисовки
     */
    public MenuBarController(SceneManager sceneManager, RenderEngine renderEngine, Canvas canvas) {
        this.sceneManager = sceneManager;
        this.renderEngine = renderEngine;
        this.canvas = canvas;
    }

    /**
     * Инициализирует меню, добавляет пункты и устанавливает обработчики событий.
     *
     * @param menuBar MenuBar, который нужно инициализировать.
     */
    public void initializeMenu(MenuBar menuBar) {
        Menu fileMenu = new Menu("File");

        MenuItem openItem = new MenuItem("Open");
        MenuItem saveItem = new MenuItem("Save");

        openItem.setOnAction(e -> onOpenModel());
        saveItem.setOnAction(e -> onSaveModel());

        fileMenu.getItems().addAll(openItem, saveItem);

        menuBar.getMenus().add(fileMenu);
    }

    /**
     * Обработчик действия для пункта меню "Open".
     */
    private void onOpenModel() {
        LoggerUtil.info("onOpenModel() called");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Model");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (selectedFile != null) {
            LoggerUtil.info("Selected file: " + selectedFile.getAbsolutePath());
            Model loadedModel = ObjReader.read(selectedFile);
            if (!loadedModel.getVertices().isEmpty() && !loadedModel.getPolygons().isEmpty()) {
                SceneModel sceneModel = new SceneModel(loadedModel);
                sceneManager.addModel(sceneModel);
                LoggerUtil.info("Model loaded and added to scene: " + selectedFile.getName());
                renderEngine.render(canvas, sceneManager.getModelObservableList());
            } else {
                ErrorHandler.showError("Invalid Model", "The selected model is empty or invalid.");
                LoggerUtil.warn("Loaded model is empty or invalid: " + selectedFile.getName());
            }
        } else {
            LoggerUtil.info("File selection cancelled.");
        }
    }

    /**
     * Обработчик действия для пункта меню "Save".
     */
    private void onSaveModel() {
        LoggerUtil.info("onSaveModel() called");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Model");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File saveFile = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (saveFile != null) {
            LoggerUtil.info("Saving model to: " + saveFile.getAbsolutePath());
            SceneModel activeModel = sceneManager.getFirstActiveModel();
            if (activeModel != null) {
                try {
                    ObjWriter.write(saveFile, activeModel.getModel());
                    LoggerUtil.info("Model successfully saved to: " + saveFile.getName());
                } catch (Exception e) {
                    ErrorHandler.showError("Save Error", "Could not save model: " + e.getMessage());
                    LoggerUtil.error("Error saving model: " + e.getMessage());
                }
            } else {
                ErrorHandler.showError("No Active Model", "Please activate a model to save.");
                LoggerUtil.warn("No active model to save.");
            }
        } else {
            LoggerUtil.info("Save file selection cancelled.");
        }
    }
}
