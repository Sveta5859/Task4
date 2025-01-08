package com.myproject.ui;

import com.myproject.common.ErrorHandler;
import com.myproject.io.ObjReader;
import com.myproject.io.ObjWriter;
import com.myproject.model.Model;
import com.myproject.scene.SceneManager;
import com.myproject.scene.SceneModel;
import com.myproject.math.Vector3f;
import com.myproject.render_engine.Camera;
import com.myproject.render_engine.RenderEngine;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class GuiController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Canvas canvas;

    private SceneManager sceneManager;
    private RenderEngine renderEngine;
    private ModelSelectionPanel modelSelectionPanel;
    private DeleteGeometryPanel deleteGeometryPanel;
    private TransformationPanel transformationPanel;

    private Vector3f cameraPosition = new Vector3f(0,0,5);
    private Vector3f cameraTarget = new Vector3f(0,0,0);
    private Vector3f cameraUp = new Vector3f(0,1,0);
    private Camera camera;

    private double lastX,lastY;
    private boolean leftDrag=false;
    private boolean rightDrag=false;

    @FXML
    public void initialize() {
        sceneManager = new SceneManager();

        camera = new Camera(cameraPosition, cameraTarget, cameraUp,60.0f,(float)(canvas.getWidth()/canvas.getHeight()),0.1f,1000f);
        renderEngine = new RenderEngine(camera,(int)canvas.getWidth(),(int)canvas.getHeight());
        renderEngine.setWireframe(false);

        modelSelectionPanel = new ModelSelectionPanel(sceneManager, this);
        rootPane.setRight(modelSelectionPanel.getNode());

        deleteGeometryPanel = new DeleteGeometryPanel(sceneManager, this);
        transformationPanel = new TransformationPanel(sceneManager, this);
        rootPane.setBottom(transformationPanel.getNode());

        initializeMenu();

        canvas.setOnScroll(this::onScrollZoom);
        canvas.setOnMousePressed(e->{
            lastX=e.getX();
            lastY=e.getY();
            if(e.getButton()==MouseButton.PRIMARY) leftDrag=true;
            if(e.getButton()==MouseButton.SECONDARY) rightDrag=true;
        });
        canvas.setOnMouseReleased(e->{
            if(e.getButton()==MouseButton.PRIMARY) leftDrag=false;
            if(e.getButton()==MouseButton.SECONDARY) rightDrag=false;
        });
        canvas.setOnMouseDragged(e->{
            double dx = e.getX()-lastX;
            double dy = e.getY()-lastY;
            lastX=e.getX();
            lastY=e.getY();

            if(leftDrag) {
                float factor=0.01f;
                Vector3f forward = new Vector3f(cameraTarget.getX()-cameraPosition.getX(),
                        cameraTarget.getY()-cameraPosition.getY(),
                        cameraTarget.getZ()-cameraPosition.getZ());
                float flen = forward.length();
                if(flen>1e-9) {
                    forward.setX(forward.getX()/flen);
                    forward.setY(forward.getY()/flen);
                    forward.setZ(forward.getZ()/flen);
                }
                Vector3f upVec = new Vector3f(0,1,0);
                Vector3f right = cross(forward, upVec);
                float rlen = right.length();
                if(rlen>1e-9) {
                    right.setX(right.getX()/rlen);
                    right.setY(right.getY()/rlen);
                    right.setZ(right.getZ()/rlen);
                }
                Vector3f camUp = cross(right,forward);

                float moveX = (float)(-dx)*factor;
                float moveY = (float)(dy)*factor;

                cameraPosition.setX(cameraPosition.getX()+right.getX()*moveX + camUp.getX()*moveY);
                cameraPosition.setY(cameraPosition.getY()+right.getY()*moveX + camUp.getY()*moveY);
                cameraPosition.setZ(cameraPosition.getZ()+right.getZ()*moveX + camUp.getZ()*moveY);

                cameraTarget.setX(cameraTarget.getX()+right.getX()*moveX + camUp.getX()*moveY);
                cameraTarget.setY(cameraTarget.getY()+right.getY()*moveX + camUp.getY()*moveY);
                cameraTarget.setZ(cameraTarget.getZ()+right.getZ()*moveX + camUp.getZ()*moveY);
            }

            if(rightDrag) {
                float rotFactor=0.5f;
                float angleY = (float)(-dx*rotFactor);
                float angleX = (float)(-dy*rotFactor);

                Vector3f dir = new Vector3f(cameraPosition.getX()-cameraTarget.getX(),
                        cameraPosition.getY()-cameraTarget.getY(),
                        cameraPosition.getZ()-cameraTarget.getZ());
                float x=dir.getX();
                float y=dir.getY();
                float z=dir.getZ();

                float theta = (float)Math.toRadians(angleY);
                float phi   = (float)Math.toRadians(angleX);

                float cosY=(float)Math.cos(theta);
                float sinY=(float)Math.sin(theta);
                float nx = x*cosY - z*sinY;
                float nz = x*sinY + z*cosY;

                x=nx;z=nz;

                float cosX=(float)Math.cos(phi);
                float sinX=(float)Math.sin(phi);
                float ny = y*cosX - z*sinX;
                float nz2= y*sinX + z*cosX;

                y=ny;z=nz2;

                cameraPosition.setX(cameraTarget.getX()+x);
                cameraPosition.setY(cameraTarget.getY()+y);
                cameraPosition.setZ(cameraTarget.getZ()+z);
            }

            updateCameraParams();
            renderScene();
        });

        renderScene();
    }

    private void onScrollZoom(ScrollEvent e) {
        float factor=0.1f;
        float dz = (float)e.getDeltaY()*factor;
        Vector3f dir = new Vector3f(cameraPosition.getX()-cameraTarget.getX(),
                cameraPosition.getY()-cameraTarget.getY(),
                cameraPosition.getZ()-cameraTarget.getZ());
        float len=dir.length();
        if(len - dz > 0.1f) {
            float scale=(len - dz)/len;
            cameraPosition.setX(cameraTarget.getX()+dir.getX()*scale);
            cameraPosition.setY(cameraTarget.getY()+dir.getY()*scale);
            cameraPosition.setZ(cameraTarget.getZ()+dir.getZ()*scale);
        }
        updateCameraParams();
        renderScene();
    }

    private void initializeMenu() {
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(e -> onOpenModel());
        MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(e -> onSaveModel());
        fileMenu.getItems().addAll(openItem, saveItem);

        Menu editMenu = new Menu("Edit");
        MenuItem deleteGeoItem = new MenuItem("Delete Geometry");
        deleteGeoItem.setOnAction(e -> {
            if (rootPane.getLeft() == null) {
                rootPane.setLeft(deleteGeometryPanel.getNode());
            } else {
                rootPane.setLeft(null);
            }
        });
        editMenu.getItems().add(deleteGeoItem);

        menuBar.getMenus().addAll(fileMenu, editMenu);
    }

    public void renderScene() {
        long start=System.nanoTime();
        renderEngine.render(canvas, sceneManager.getModelObservableList());
        long end=System.nanoTime();
        // Uncomment for minimal logging:
        //com.myproject.common.LoggerUtil.info("renderScene took "+((end-start)/1_000_000)+" ms");
    }

    @FXML
    private void onOpenModel() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open Model");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fc.showOpenDialog(getPrimaryStage());
        if (file != null) {
            SceneModel sm = com.myproject.io.ObjReader.createSceneModel(file);
            if (!sm.getModel().getVertices().isEmpty() && !sm.getModel().getPolygons().isEmpty()) {
                sceneManager.addModel(sm);
                renderScene();
            } else {
                ErrorHandler.showError("Invalid Model", "Model is empty or invalid.");
            }
        }
    }

    @FXML
    private void onSaveModel() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Model");
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("OBJ Files", "*.obj"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        File file = fc.showSaveDialog(getPrimaryStage());
        if (file != null) {
            if (sceneManager.getActiveModels().isEmpty()) {
                ErrorHandler.showError("No Active Model", "Select a model to save.");
                return;
            }
            try {
                ObjWriter.write(file, sceneManager.getActiveModels().get(0).getModel());
            } catch (Exception e) {
                ErrorHandler.showError("Save Error", "Could not save model: " + e.getMessage());
            }
        }
    }

    private Stage getPrimaryStage() {
        return (Stage)rootPane.getScene().getWindow();
    }

    private void updateCameraParams(){
        camera.setPosition(cameraPosition);
        camera.setTarget(cameraTarget);
        // Не пересоздаём RenderEngine, он уже есть
        // RenderEngine будет использовать обновлённую камеру при рендере.
    }

    private Vector3f cross(Vector3f a, Vector3f b) {
        float cx = a.getY()*b.getZ()-a.getZ()*b.getY();
        float cy = a.getZ()*b.getX()-a.getX()*b.getZ();
        float cz = a.getX()*b.getY()-a.getY()*b.getX();
        return new Vector3f(cx,cy,cz);
    }
}
