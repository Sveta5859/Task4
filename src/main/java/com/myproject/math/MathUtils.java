package com.myproject.math;

public class MathUtils {
    public static void normalize(Vector3f v){
        float len=v.length();
        if(len>1e-9){
            v.setX(v.getX()/len);
            v.setY(v.getY()/len);
            v.setZ(v.getZ()/len);
        }
    }

    public static float dot(Vector3f a,Vector3f b){
        return a.getX()*b.getX()+a.getY()*b.getY()+a.getZ()*b.getZ();
    }

    public static Vector3f cross(Vector3f a,Vector3f b){
        float cx=a.getY()*b.getZ()-a.getZ()*b.getY();
        float cy=a.getZ()*b.getX()-a.getX()*b.getZ();
        float cz=a.getX()*b.getY()-a.getY()*b.getX();
        return new Vector3f(cx,cy,cz);
    }

    public static Matrix4f createTranslation(float tx,float ty,float tz){
        Matrix4f mat=new Matrix4f();
        float[][] d=mat.getData();
        d[0][3]=tx;d[1][3]=ty;d[2][3]=tz;
        return mat;
    }

    public static Matrix4f createRotationX(float angleDeg) {
        Matrix4f mat=new Matrix4f();
        float a=(float)Math.toRadians(angleDeg);
        float c=(float)Math.cos(a);
        float s=(float)Math.sin(a);
        float[][] d=mat.getData();
        d[1][1]=c;d[1][2]=-s;
        d[2][1]=s;d[2][2]=c;
        return mat;
    }

    public static Matrix4f createRotationY(float angleDeg) {
        Matrix4f mat=new Matrix4f();
        float a=(float)Math.toRadians(angleDeg);
        float c=(float)Math.cos(a);
        float s=(float)Math.sin(a);
        float[][] d=mat.getData();
        d[0][0]=c;d[0][2]=s;
        d[2][0]=-s;d[2][2]=c;
        return mat;
    }

    public static Matrix4f createRotationZ(float angleDeg) {
        Matrix4f mat=new Matrix4f();
        float a=(float)Math.toRadians(angleDeg);
        float c=(float)Math.cos(a);
        float s=(float)Math.sin(a);
        float[][] d=mat.getData();
        d[0][0]=c;d[0][1]=-s;
        d[1][0]=s;d[1][1]=c;
        return mat;
    }

    public static Matrix4f createScaling(float sx,float sy,float sz) {
        Matrix4f mat=new Matrix4f();
        float[][] d=mat.getData();
        d[0][0]=sx;d[1][1]=sy;d[2][2]=sz;
        return mat;
    }

    public static float[] multiplyMatrixByVector(Matrix4f mat,float x,float y,float z) {
        float[][] M=mat.getData();
        float X=M[0][0]*x+M[0][1]*y+M[0][2]*z+M[0][3]*1;
        float Y=M[1][0]*x+M[1][1]*y+M[1][2]*z+M[1][3]*1;
        float Z=M[2][0]*x+M[2][1]*y+M[2][2]*z+M[2][3]*1;
        float W=M[3][0]*x+M[3][1]*y+M[3][2]*z+M[3][3]*1;
        return new float[]{X,Y,Z,W};
    }

    public static Vector3f multiplyMatrixByVector3f(Matrix4f mat,float x,float y,float z) {
        float[] res=multiplyMatrixByVector(mat,x,y,z);
        float w=res[3]!=0?res[3]:1;
        return new Vector3f(res[0]/w,res[1]/w,res[2]/w);
    }
}
