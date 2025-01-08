package com.myproject.ui;

import com.myproject.common.ErrorHandler;
import com.myproject.model.MeshModifier;
import com.myproject.scene.SceneManager;
import com.myproject.scene.SceneModel;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class DeleteGeometryPanel {

    private final SceneManager sceneManager;
    private final GuiController guiController;//перерисовка сцены
    private final VBox root;
    private final TextField vertexIndexField;
    private final TextField polygonIndexField;
    private final Button deleteVertexButton;
    private final Button deletePolygonButton;

    public DeleteGeometryPanel(SceneManager sceneManager, GuiController guiController) {
        this.sceneManager = sceneManager;
        this.guiController = guiController;
        root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        vertexIndexField = new TextField();
        vertexIndexField.setPromptText("Vertex Index");

        polygonIndexField = new TextField();
        polygonIndexField.setPromptText("Polygon Index");

        deleteVertexButton = new Button("Delete Vertex");
        deleteVertexButton.setOnAction(e -> {
            SceneModel activeModel = sceneManager.getFirstActiveModel();
            if (activeModel != null) {
                try {
                    int idx = Integer.parseInt(vertexIndexField.getText());
                    MeshModifier.deleteVertex(activeModel.getModel(), idx);
                    guiController.renderScene();
                } catch (NumberFormatException nfe) {
                    ErrorHandler.showError("Invalid Input", "Vertex index must be a number.");
                } catch (Exception ex) {
                    ErrorHandler.showError("Error", "Could not delete vertex: " + ex.getMessage());
                }
            } else {
                ErrorHandler.showError("No Active Model", "Select a model first.");
            }
        });

        deletePolygonButton = new Button("Delete Polygon");
        deletePolygonButton.setOnAction(e -> {
            SceneModel activeModel = sceneManager.getFirstActiveModel();
            if (activeModel != null) {
                try {
                    int idx = Integer.parseInt(polygonIndexField.getText());
                    MeshModifier.deletePolygon(activeModel.getModel(), idx);
                    guiController.renderScene();
                } catch (NumberFormatException nfe) {
                    ErrorHandler.showError("Invalid Input", "Polygon index must be a number.");
                } catch (Exception ex) {
                    ErrorHandler.showError("Error", "Could not delete polygon: " + ex.getMessage());
                }
            } else {
                ErrorHandler.showError("No Active Model", "Select a model first.");
            }
        });

        root.getChildren().addAll(
                new Label("Delete Geometry"),
                new Label("Vertex:"), vertexIndexField, deleteVertexButton,
                new Label("Polygon:"), polygonIndexField, deletePolygonButton
        );
    }

    public VBox getNode() {
        return root;
    }
}
