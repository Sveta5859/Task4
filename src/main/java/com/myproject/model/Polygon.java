package com.myproject.model;

import java.util.List;

public class Polygon {
    private List<Integer> vertexIndices;

    public Polygon(List<Integer> vertexIndices) {

        this.vertexIndices = vertexIndices;
    }

    public List<Integer> getVertexIndices() {
//возвращает список индексов вершин полигона
        return vertexIndices;
    }

    public boolean isValid() {
//проверяет, является ли полигон допустимым
        return vertexIndices.size()==3;
    }
}
