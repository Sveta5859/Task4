package com.myproject.render_engine;

import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Rasterizer {
    private int width;
    private int height;
    private double[][] zBuffer;

    public Rasterizer(int width,int height) {
        this.width=width;
        this.height=height;
        zBuffer = new double[width][height];
        clearZBuffer();
    }

    public void clearZBuffer() {
        for (int x=0;x<width;x++) {
            for (int y=0;y<height;y++) {
                zBuffer[x][y] = Double.POSITIVE_INFINITY;
            }
        }
    }

    public void drawLine(PixelWriter pw,int x0,int y0,int x1,int y1, Color color,double z) {
        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);
        int sx = x0<x1?1:-1;
        int sy = y0<y1?1:-1;
        int err = dx-dy;
        int x=x0;int y=y0;
        while(true) {
            putPixel(pw,x,y,z,color);
            if(x==x1 && y==y1) break;
            int e2 = 2*err;
            if(e2>-dy){err-=dy;x+=sx;}
            if(e2<dx){err+=dx;y+=sy;}
        }
    }

    private void putPixel(PixelWriter pw,int x,int y,double z,Color c) {
        if(x<0||x>=width||y<0||y>=height)return;
        if(z<zBuffer[x][y]) {
            zBuffer[x][y]=z;
            pw.setColor(x,y,c);
        }
    }

    public void fillTriangle(PixelWriter pw,float[][] vertices, Color color) {
        if(vertices[0][1]>vertices[1][1]) swap(vertices,0,1);
        if(vertices[1][1]>vertices[2][1]) swap(vertices,1,2);
        if(vertices[0][1]>vertices[1][1]) swap(vertices,0,1);

        float x1=vertices[0][0], y1=vertices[0][1], z1=vertices[0][2];
        float x2=vertices[1][0], y2=vertices[1][1], z2=vertices[1][2];
        float x3=vertices[2][0], y3=vertices[2][1], z3=vertices[2][2];

        if(y2==y3) {
            fillTriangleBottom(pw,x1,y1,z1,x2,y2,z2,x3,y3,z3,color);
        } else if(y1==y2) {
            fillTriangleTop(pw,x1,y1,z1,x2,y2,z2,x3,y3,z3,color);
        } else {
            float alphaSplit = (y2 - y1)/(y3 - y1);
            float x4 = x1+(x3-x1)*alphaSplit;
            float z4 = z1+(z3-z1)*alphaSplit;
            fillTriangleBottom(pw,x1,y1,z1,x2,y2,z2,x4,y2,z4,color);
            fillTriangleTop(pw,x2,y2,z2,x4,y2,z4,x3,y3,z3,color);
        }
    }

    private void fillTriangleBottom(PixelWriter pw,float x1,float y1,float z1,
                                    float x2,float y2,float z2,
                                    float x3,float y3,float z3,Color color) {
        float invSlope1 = (x2 - x1)/(y2 - y1);
        float invSlope2 = (x3 - x1)/(y3 - y1);
        float invSlopeZ1 = (z2 - z1)/(y2 - y1);
        float invSlopeZ2 = (z3 - z1)/(y3 - y1);

        float curX1 = x1;float curX2 = x1;
        float curZ1 = z1;float curZ2=z1;

        for(int scanlineY=(int)y1;scanlineY<=(int)y2;scanlineY++) {
            drawScanLine(pw,scanlineY,curX1,curZ1,curX2,curZ2,color);
            curX1+=invSlope1;
            curX2+=invSlope2;
            curZ1+=invSlopeZ1;
            curZ2+=invSlopeZ2;
        }
    }

    private void fillTriangleTop(PixelWriter pw,float x1,float y1,float z1,
                                 float x2,float y2,float z2,
                                 float x3,float y3,float z3,Color color) {
        float invSlope1 = (x3 - x1)/(y3 - y1);
        float invSlope2 = (x3 - x2)/(y3 - y2);
        float invSlopeZ1=(z3-z1)/(y3-y1);
        float invSlopeZ2=(z3-z2)/(y3-y2);

        float curX1 = x3;float curX2 = x3;
        float curZ1 = z3;float curZ2=z3;

        for(int scanlineY=(int)y3;scanlineY>(int)y1;scanlineY--) {
            curX1-=invSlope1;
            curX2-=invSlope2;
            curZ1-=invSlopeZ1;
            curZ2-=invSlopeZ2;
            drawScanLine(pw,scanlineY,curX1,curZ1,curX2,curZ2,color);
        }
    }

    private void drawScanLine(PixelWriter pw,int y,float x1,float z1,float x2,float z2,Color color) {
        if(y<0||y>=height)return;
        if(x1>x2) {
            float tx=x1;x1=x2;x2=tx;
            float tz=z1;z1=z2;z2=tz;
        }

        int startX=(int)x1;int endX=(int)x2;
        float dz=(z2-z1)/(x2-x1+0.000001f);
        float curZ=z1;
        for(int x=startX;x<=endX;x++) {
            putPixel(pw,x,y,curZ,color);
            curZ+=dz;
        }
    }

    private void swap(float[][] arr,int i,int j) {
        float[] temp = arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
}
