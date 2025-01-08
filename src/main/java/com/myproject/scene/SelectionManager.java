package com.myproject.scene;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {
    private final List<SceneModel> selectedModels;

    public SelectionManager() {
        this.selectedModels = new ArrayList<>();
    }

    public void selectModel(SceneModel model) {
        if (!selectedModels.contains(model)) {
            selectedModels.add(model);
        }
    }

    public void deselectModel(SceneModel model) {
        selectedModels.remove(model);
    }

    public void clearSelection() {
        selectedModels.clear();
    }

    public List<SceneModel> getSelectedModels() {
        return selectedModels;
    }
}
