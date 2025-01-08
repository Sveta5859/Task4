package com.myproject.model;

public class Vertex {
    private float x,y,z;
    public Vertex(float x,float y,float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }//улучшает инкапсуляцию и позволяет контролировать доступ к данным
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getZ(){
        return z;
    }
}
