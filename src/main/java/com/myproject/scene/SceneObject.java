/*package com.myproject.scene;

import com.myproject.math.Matrix4f;
import com.myproject.math.Vector3f;
import com.myproject.math.MathUtils;


public abstract class SceneObject {
    protected Vector3f position;
    protected Vector3f rotation;
    protected Vector3f scale;

    public SceneObject() {
        this.position = new Vector3f(0,0,0);
        this.rotation = new Vector3f(0,0,0);
        this.scale = new Vector3f(1,1,1);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.setX(x);
        this.position.setY(y);
        this.position.setZ(z);
    }

    public void translate(float dx, float dy, float dz) {
        this.position.setX(position.getX() + dx);
        this.position.setY(position.getY() + dy);
        this.position.setZ(position.getZ() + dz);
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float rotX, float rotY, float rotZ) {
        this.rotation.setX(rotX);
        this.rotation.setY(rotY);
        this.rotation.setZ(rotZ);
    }

    public void rotateX(float dAngleX) {
        this.rotation.setX(rotation.getX() + dAngleX);
    }

    public void rotateY(float dAngleY) {
        this.rotation.setY(rotation.getY() + dAngleY);
    }

    public void rotateZ(float dAngleZ) {
        this.rotation.setZ(rotation.getZ() + dAngleZ);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float sx, float sy, float sz) {
        this.scale.setX(sx);
        this.scale.setY(sy);
        this.scale.setZ(sz);
    }

    public void uniformScale(float factor) {
        this.scale.setX(scale.getX()*factor);
        this.scale.setY(scale.getY()*factor);
        this.scale.setZ(scale.getZ()*factor);
    }

    public Matrix4f getTransformMatrix() {
        Matrix4f scaleM = MathUtils.createScaling(scale.getX(), scale.getY(), scale.getZ());
        Matrix4f rotX = MathUtils.createRotationX(rotation.getX());
        Matrix4f rotY = MathUtils.createRotationY(rotation.getY());
        Matrix4f rotZ = MathUtils.createRotationZ(rotation.getZ());
        Matrix4f translation = MathUtils.createTranslation(position.getX(), position.getY(), position.getZ());

        Matrix4f result = Matrix4f.multiply(translation,
                Matrix4f.multiply(rotZ,
                        Matrix4f.multiply(rotY,
                                Matrix4f.multiply(rotX, scaleM))));
        return result;
    }

    public void resetTransform() {
        this.position.setX(0); this.position.setY(0); this.position.setZ(0);
        this.rotation.setX(0); this.rotation.setY(0); this.rotation.setZ(0);
        this.scale.setX(1);    this.scale.setY(1);    this.scale.setZ(1);
    }
}*/
