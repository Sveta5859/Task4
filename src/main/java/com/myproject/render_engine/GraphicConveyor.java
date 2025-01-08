package com.myproject.render_engine;

import com.myproject.math.Matrix4f;
import com.myproject.math.Vector3f;
import com.myproject.math.MathUtils;

public class GraphicConveyor {
    public static Matrix4f lookAt(Vector3f position, Vector3f target, Vector3f up) {
        Vector3f f = new Vector3f(target.getX()-position.getX(), target.getY()-position.getY(), target.getZ()-position.getZ());//Вычисляет вектор «вперёд»
        MathUtils.normalize(f);
        Vector3f s = MathUtils.cross(f, up);//Вычисляет вектор «вправо»
        MathUtils.normalize(s);
        Vector3f u = MathUtils.cross(s,f);//Вычисляет вектор «вверх»

        Matrix4f mat = new Matrix4f();
        float[][] M = mat.getData();
        M[0][0]=s.getX(); M[0][1]=s.getY(); M[0][2]=s.getZ(); M[0][3]=-MathUtils.dot(s, position);
        M[1][0]=u.getX(); M[1][1]=u.getY(); M[1][2]=u.getZ(); M[1][3]=-MathUtils.dot(u, position);
        M[2][0]=-f.getX();M[2][1]=-f.getY();M[2][2]=-f.getZ();M[2][3]= MathUtils.dot(f, position);
        M[3][0]=0;        M[3][1]=0;        M[3][2]=0;        M[3][3]=1;
        return mat;
    }

    public static Matrix4f perspective(float fovRad, float aspect, float znear, float zfar) {
        float f = 1.0f/(float)Math.tan(fovRad/2.0);//Вычисляет коэффициент f на основе угла обзора
        Matrix4f mat = new Matrix4f();
        float[][] M = mat.getData();
        M[0][0]=f/aspect;M[0][1]=0;M[0][2]=0;                             M[0][3]=0;
        M[1][0]=0;       M[1][1]=f;M[1][2]=0;                             M[1][3]=0;
        M[2][0]=0;       M[2][1]=0;M[2][2]=(zfar+znear)/(znear-zfar);    M[2][3]=(2*zfar*znear)/(znear-zfar);
        M[3][0]=0;       M[3][1]=0;M[3][2]=-1;                           M[3][3]=0;
        return mat;
    }
}
