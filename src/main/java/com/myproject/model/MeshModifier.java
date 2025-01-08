package com.myproject.model;

import java.util.Iterator;

public class MeshModifier {
    public static void deleteVertex(Model model, int index) {
        if (index<0 || index>=model.getVertices().size())
            throw new IndexOutOfBoundsException("Vertex index out of range");
        model.getVertices().remove(index);

        Iterator<Polygon> it = model.getPolygons().iterator();
        while(it.hasNext()) {  //обход всех полигонов модели
            Polygon p = it.next();
            boolean removePoly = false;
            for (int i=0;i<p.getVertexIndices().size();i++) {
                int oldIdx = p.getVertexIndices().get(i);
                if (oldIdx == index) {
                    removePoly = true;
                    break;
                } else if (oldIdx > index) {
                    p.getVertexIndices().set(i, oldIdx-1);
                }
            }
            if (removePoly || !p.isValid()) {
                it.remove();
            }
        }
    }

    public static void deletePolygon(Model model, int index) {
        if (index<0 || index>=model.getPolygons().size())
            throw new IndexOutOfBoundsException("Polygon index out of range");
        model.getPolygons().remove(index);
    }
}
