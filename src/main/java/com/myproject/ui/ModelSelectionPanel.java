package com.myproject.ui;

import com.myproject.common.LoggerUtil;
import com.myproject.scene.SceneManager;
import com.myproject.scene.SceneModel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ModelSelectionPanel {

    private final SceneManager sceneManager;
    private final GuiController guiController;
    private final VBox root;
    private final ListView<SceneModel> listView;
    private final Button activateButton;
    private final Button deactivateButton;

    public ModelSelectionPanel(SceneManager sceneManager, GuiController guiController) {
        this.sceneManager = sceneManager;
        this.guiController = guiController;

        root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        Label label = new Label("Models:");
        listView = new ListView<>();
        listView.setItems(sceneManager.getModelObservableList());

        activateButton = new Button("Activate");
        activateButton.setOnAction(e -> {
            LoggerUtil.info("Activate button clicked");
            SceneModel selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                sceneManager.setActiveModel(selected);
                LoggerUtil.info("Model activated: " + selected);
                guiController.renderScene();
            } else {
                LoggerUtil.info("No model selected for activation.");
            }
        });

        deactivateButton = new Button("Deactivate");
        deactivateButton.setOnAction(e -> {
            LoggerUtil.info("Deactivate button clicked, deactivating all models");
            sceneManager.deactivateAllModels();
            guiController.renderScene();
        });

        root.getChildren().addAll(label, listView, activateButton, deactivateButton);
    }

    public VBox getNode() {
        return root;
    }
}
