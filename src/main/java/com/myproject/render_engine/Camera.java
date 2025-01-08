package com.myproject.render_engine;

import com.myproject.math.Matrix4f;
import com.myproject.math.Vector3f;
import com.myproject.math.MathUtils;

public class Camera {
    private Vector3f position;
    private Vector3f target;
    private Vector3f up;

    private float fov;      //Угол обзора по вертикали в градусах
    private float aspect;  //Соотношение сторон (ширина к высоте) видового экрана
    private float znear;  //Ближняя плоскость отсечения
    private float zfar;  //Дальняя плоскость отсечения

    public Camera(Vector3f position, Vector3f target, Vector3f up, float fov, float aspect, float znear, float zfar) {
        this.position = position;
        this.target = target;
        this.up = up;
        this.fov = fov;
        this.aspect = aspect;
        this.znear = znear;
        this.zfar = zfar;
    }

    public void setPosition(Vector3f position) {  //для установки позиции камеры

        this.position = position;
    }

    public void setTarget(Vector3f target) { //для установки точки, на которую направлена камера

        this.target = target;
    }

    public Matrix4f getViewMatrix() {  //возвращает видовую матрицу

        return GraphicConveyor.lookAt(position, target, up);
    }

    public Matrix4f getProjectionMatrix() {
        return GraphicConveyor.perspective((float)Math.toRadians(fov), aspect, znear, zfar);
    }
}
