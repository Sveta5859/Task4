package com.myproject.ui;

import com.myproject.common.LoggerUtil;
import com.myproject.scene.SceneManager;
import com.myproject.scene.SceneModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

public class TransformationPanel {

    private final SceneManager sceneManager;
    private final GuiController guiController;
    private final GridPane root;

    private Slider rotXSlider;
    private Slider rotYSlider;
    private Slider rotZSlider;

    private Slider scaleXSlider;
    private Slider scaleYSlider;
    private Slider scaleZSlider;

    private Slider posXSlider;
    private Slider posYSlider;
    private Slider posZSlider;

    public TransformationPanel(SceneManager sceneManager, GuiController guiController) {
        this.sceneManager = sceneManager;
        this.guiController = guiController;

        root = new GridPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);
        root.setVgap(10);

        Label rotXLabel = new Label("Rotate X:");
        rotXSlider = createSlider(0, 360, 0);
        rotXSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label rotYLabel = new Label("Rotate Y:");
        rotYSlider = createSlider(0, 360, 0);
        rotYSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label rotZLabel = new Label("Rotate Z:");
        rotZSlider = createSlider(0, 360, 0);
        rotZSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label scaleXLabel = new Label("Scale X:");
        scaleXSlider = createSlider(0.1, 5.0, 1.0);
        scaleXSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label scaleYLabel = new Label("Scale Y:");
        scaleYSlider = createSlider(0.1, 5.0, 1.0);
        scaleYSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label scaleZLabel = new Label("Scale Z:");
        scaleZSlider = createSlider(0.1, 5.0, 1.0);
        scaleZSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label posXLabel = new Label("Pos X:");
        posXSlider = createSlider(-10,10,0);
        posXSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label posYLabel = new Label("Pos Y:");
        posYSlider = createSlider(-10,10,0);
        posYSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        Label posZLabel = new Label("Pos Z:");
        posZSlider = createSlider(-10,10,0);
        posZSlider.valueProperty().addListener((obs, oldVal, newVal) -> applyTransform());

        root.add(rotXLabel, 0, 0); root.add(rotXSlider, 1,0);
        root.add(rotYLabel, 0, 1); root.add(rotYSlider, 1,1);
        root.add(rotZLabel, 0, 2); root.add(rotZSlider, 1,2);

        root.add(scaleXLabel, 2,0); root.add(scaleXSlider, 3,0);
        root.add(scaleYLabel, 2,1); root.add(scaleYSlider, 3,1);
        root.add(scaleZLabel, 2,2); root.add(scaleZSlider, 3,2);

        root.add(posXLabel, 4,0); root.add(posXSlider,5,0);
        root.add(posYLabel, 4,1); root.add(posYSlider,5,1);
        root.add(posZLabel, 4,2); root.add(posZSlider,5,2);
    }

    private Slider createSlider(double min, double max, double value) {
        Slider s = new Slider(min,max,value);
        s.setShowTickMarks(true);
        s.setShowTickLabels(true);
        return s;
    }

    private void applyTransform() {
        SceneModel active = sceneManager.getFirstActiveModel();
        if (active != null) {
            float rx = (float)rotXSlider.getValue();
            float ry = (float)rotYSlider.getValue();
            float rz = (float)rotZSlider.getValue();

            float sx = (float)scaleXSlider.getValue();
            float sy = (float)scaleYSlider.getValue();
            float sz = (float)scaleZSlider.getValue();

            float px = (float)posXSlider.getValue();
            float py = (float)posYSlider.getValue();
            float pz = (float)posZSlider.getValue();

            active.setRotation(rx, ry, rz);
            active.setScale(sx, sy, sz);
            active.setPosition(px, py, pz);

            //LoggerUtil.info("TransformationPanel: Applied transform to active model: R("+rx+","+ry+","+rz+") S("+sx+","+sy+","+sz+") P("+px+","+py+","+pz+")");
            guiController.renderScene();
        } else {
            LoggerUtil.info("TransformationPanel: No active model to transform.");
        }
    }

    public GridPane getNode() {
        return root;
    }
}
