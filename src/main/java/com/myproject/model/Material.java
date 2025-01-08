package com.myproject.model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Material {
    private Color ambient = Color.color(1,1,1);
    private Color diffuse = Color.color(0.8,0.8,0.8);
    private Color specular = Color.color(0.5,0.5,0.5);
    private Color emissive = Color.color(0,0,0);
    private float shininess = 96.078431f;
    private float indexOfRefraction = 1.0f;
    private float opacity = 1.0f;
    private int illum = 2;

    private Image texture = null;
    private boolean hasTexture = false;

    public void setAmbient(Color c){ambient=c;}
    public Color getAmbient(){return ambient;}

    public void setDiffuse(Color c){diffuse=c;}
    public Color getDiffuse(){return diffuse;}

    public void setSpecular(Color c){specular=c;}
    public Color getSpecular(){return specular;}

    public void setEmissive(Color c){emissive=c;}
    public Color getEmissive(){return emissive;}

    public void setShininess(float ns){shininess=ns;}
    public float getShininess(){return shininess;}

    public void setIndexOfRefraction(float ni){indexOfRefraction=ni;}
    public float getIndexOfRefraction(){return indexOfRefraction;}

    public void setOpacity(float d){opacity=d;}
    public float getOpacity(){return opacity;}

    public void setIllum(int i){illum=i;}
    public int getIllum(){return illum;}

    public void setTexture(Image tex) {
        this.texture=tex;
        this.hasTexture=true;
    }

    public boolean hasTexture(){return hasTexture;}
    public Image getTexture(){return texture;}
}
