package com.myproject.math;

public class Matrix4f {
    private float[][] m;

    public Matrix4f() {
        m=new float[4][4];
        setIdentity();  //инициализация матрица как единичная
    }

    public void setIdentity(){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                m[i][j]=(i==j)?1:0;
            }
        }
    }

    public float[][] getData(){
        return m;
    }

    public void setElement(int r,int c,float val){
        m[r][c]=val;
    }

    public static Matrix4f multiply(Matrix4f a,Matrix4f b){
        Matrix4f res=new Matrix4f();
        float[][] A=a.m;
        float[][] B=b.m;
        float[][] R=res.m;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                float sum=0;
                for(int k=0;k<4;k++){  //вычисления суммы произведений элементов
                    sum+=A[i][k]*B[k][j];
                }
                R[i][j]=sum;  //элемент результирующей матрицы
            }
        }
        return res;
    }

    @Override  //строковое представление матрицы для удобного вывода
    public String toString(){
        StringBuilder sb=new StringBuilder("Matrix4f:\n");
        for(int i=0;i<4;i++){
            sb.append("[ ");
            for(int j=0;j<4;j++){
                sb.append(String.format("%8.3f ",m[i][j]));
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
