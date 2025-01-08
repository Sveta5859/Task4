package com.myproject.io;

import com.myproject.model.Model;
import com.myproject.model.Polygon;
import com.myproject.model.Vertex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ObjWriter {

    public static void write(File file, Model model) throws IOException {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Vertex v : model.getVertices()) {
                bw.write("v " + v.getX() + " " + v.getY() + " " + v.getZ());
                bw.newLine();
            }
            for (Polygon p : model.getPolygons()) {
                bw.write("f ");
                for (Integer idx : p.getVertexIndices()) {
                    bw.write((idx+1) + " ");
                }
                bw.newLine();
            }
        }
    }
}
