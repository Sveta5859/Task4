package com.myproject.model;

import java.util.List;

public class Polygon {
    private List<Integer> vertexIndices;

    public Polygon(List<Integer> vertexIndices) {
        this.vertexIndices = vertexIndices;
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public boolean isValid() {
        return vertexIndices.size()==3;
    }
}
