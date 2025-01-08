package com.myproject.scene;

import com.myproject.math.Matrix4f;
import com.myproject.math.MathUtils;
import com.myproject.math.Vector3f;
import com.myproject.model.Model;

public class SceneModel {
    private Model model;
    private boolean active;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;
    private Matrix4f transformMatrix;
    private String name;

    public SceneModel(Model model) {
        this.model = model;
        this.active = false;
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = new Vector3f(1,1,1);
        recalcTransformMatrix();
    }

    public Model getModel() { return model; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Vector3f getPosition() { return position; }
    public void setPosition(float x,float y,float z) {
        position.setX(x); position.setY(y); position.setZ(z);
        recalcTransformMatrix();
    }

    public Vector3f getRotation() { return rotation; }
    public void setRotation(float rx,float ry,float rz) {
        rotation.setX(rx); rotation.setY(ry); rotation.setZ(rz);
        recalcTransformMatrix();
    }

    public Vector3f getScale() { return scale; }
    public void setScale(float sx,float sy,float sz) {
        scale.setX(sx); scale.setY(sy); scale.setZ(sz);
        recalcTransformMatrix();
    }

    private void recalcTransformMatrix() {
        Matrix4f T = MathUtils.createTranslation(position.getX(), position.getY(), position.getZ());
        Matrix4f Rx = MathUtils.createRotationX(rotation.getX());
        Matrix4f Ry = MathUtils.createRotationY(rotation.getY());
        Matrix4f Rz = MathUtils.createRotationZ(rotation.getZ());
        Matrix4f S = MathUtils.createScaling(scale.getX(), scale.getY(), scale.getZ());

        Matrix4f R = Matrix4f.multiply(Matrix4f.multiply(Rz,Ry),Rx);
        Matrix4f M = Matrix4f.multiply(Matrix4f.multiply(T, R), S);
        this.transformMatrix = M;
    }

    public Matrix4f getTransformMatrix() { return transformMatrix; }

    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String toString() {
        if(name!=null && !name.isEmpty()) return name;
        return "SceneModel";
    }
}
