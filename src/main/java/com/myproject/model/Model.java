package com.myproject.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Model {
    private ObservableList<Vertex> vertices;  //для отслеживания изменений
    private ObservableList<Polygon> polygons;
    private Material material;

    private List<float[]> texCoords;
    private List<float[]> norms;
    private List<int[]> texFaces;
    private List<int[]> normFaces;

    public Model() {
        vertices = FXCollections.observableArrayList();
        polygons = FXCollections.observableArrayList();
        material = new Material();
    }
  //  чтобы пользовательский интерфейс мог отслеживать изменения нужны геттеры
    public ObservableList<Vertex> getVertices() {
        return vertices;
    }

    public ObservableList<Polygon> getPolygons() {
        return polygons;
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }  //Методы для добавления вершин и полигонов

    public void addPolygon(Polygon p) {
        polygons.add(p);
    }

    public Material getMaterial() {
        return material;
    }  //Методы для получения и установки материала модели

    public void setMaterial(Material m) {
        this.material=m;
    }

    public void setTexCoords(List<float[]> tcs){
        this.texCoords=tcs;
    }

    public List<float[]> getTexCoords(){
        return texCoords;
    }

    public void setNorms(List<float[]> n) {
        this.norms=n;
    }

    public List<float[]> getNorms(){
        return norms;
    }

    public void setTexFaces(List<int[]> tf) {
        this.texFaces=tf;
    }

    public List<int[]> getTexFaces(){
        return texFaces;
    }

    public void setNormFaces(List<int[]> nf) {
        this.normFaces=nf;
    }

    public List<int[]> getNormFaces(){
        return normFaces;
    }
}
//установка и получение списков