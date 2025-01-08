package com.myproject.math;

public class Vector3f {
    private float x,y,z;
    public Vector3f(){this(0,0,0);}
    public Vector3f(float x,float y,float z){this.x=x;this.y=y;this.z=z;}

    public float getX(){return x;}
    public float getY(){return y;}
    public float getZ(){return z;}

    public void setX(float x){this.x=x;}
    public void setY(float y){this.y=y;}
    public void setZ(float z){this.z=z;}

    public float length(){
        return (float)Math.sqrt(x*x+y*y+z*z);
    }

    @Override
    public String toString(){
        return "Vector3f("+x+","+y+","+z+")";
    }
}
