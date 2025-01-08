package com.myproject.scene;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SceneManager {
    private ObservableList<SceneModel> models;

    public SceneManager() {
        models = FXCollections.observableArrayList();
    }

    public void addModel(SceneModel model) {
        models.add(model);
    }

    public ObservableList<SceneModel> getModelObservableList() {
        return models;
    }

    public void setActiveModel(SceneModel model) {
        for (SceneModel m : models) {
            m.setActive(m.equals(model));
        }
    }

    public void deactivateModel(SceneModel model) {
        model.setActive(false);
    }

    public void deactivateAllModels() {
        for (SceneModel m : models) {
            m.setActive(false);
        }
    }

    public SceneModel getFirstActiveModel() {
        for (SceneModel m : models) {
            if (m.isActive()) return m;
        }
        return null;
    }

    public ObservableList<SceneModel> getActiveModels() {
        ObservableList<SceneModel> actives = FXCollections.observableArrayList();
        for (SceneModel m : models) {
            if (m.isActive()) actives.add(m);
        }
        return actives;
    }
}
