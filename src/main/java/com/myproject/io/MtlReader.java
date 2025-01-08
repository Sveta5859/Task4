package com.myproject.io;

import com.myproject.common.LoggerUtil;
import com.myproject.model.Material;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MtlReader {
    public static Map<String, Material> read(File file) {
        Map<String, Material> materials = new HashMap<>();
        if (file == null || !file.exists()) {
            LoggerUtil.warn("MtlReader: MTL file not found or null.");
            return materials;
        }
        String dir = file.getParent();

        Material current=null;
        String currentName=null;

        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while((line=br.readLine())!=null) {
                line=line.trim();
                if(line.isEmpty()||line.startsWith("#")) continue;
                if(line.startsWith("newmtl ")) {
                    currentName=line.substring(7).trim();
                    current=new Material();
                    materials.put(currentName,current);
                } else if(line.startsWith("Ka ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=4 && current!=null) {
                        float r=Float.parseFloat(parts[1]);
                        float g=Float.parseFloat(parts[2]);
                        float b=Float.parseFloat(parts[3]);
                        current.setAmbient(Color.color(r,g,b));
                    }
                } else if(line.startsWith("Kd ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=4 && current!=null) {
                        float r=Float.parseFloat(parts[1]);
                        float g=Float.parseFloat(parts[2]);
                        float b=Float.parseFloat(parts[3]);
                        current.setDiffuse(Color.color(r,g,b));
                    }
                } else if(line.startsWith("Ks ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=4 && current!=null) {
                        float r=Float.parseFloat(parts[1]);
                        float g=Float.parseFloat(parts[2]);
                        float b=Float.parseFloat(parts[3]);
                        current.setSpecular(Color.color(r,g,b));
                    }
                } else if(line.startsWith("Ke ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=4 && current!=null) {
                        float r=Float.parseFloat(parts[1]);
                        float g=Float.parseFloat(parts[2]);
                        float b=Float.parseFloat(parts[3]);
                        current.setEmissive(Color.color(r,g,b));
                    }
                } else if(line.startsWith("Ns ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=2 && current!=null) {
                        float ns=Float.parseFloat(parts[1]);
                        current.setShininess(ns);
                    }
                } else if(line.startsWith("Ni ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=2 && current!=null) {
                        float ni=Float.parseFloat(parts[1]);
                        current.setIndexOfRefraction(ni);
                    }
                } else if(line.startsWith("d ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=2 && current!=null) {
                        float d=Float.parseFloat(parts[1]);
                        current.setOpacity(d);
                    }
                } else if(line.startsWith("illum ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length>=2 && current!=null) {
                        int il=Integer.parseInt(parts[1]);
                        current.setIllum(il);
                    }
                } else if(line.startsWith("map_Kd ")) {
                    if(current!=null) {
                        String map=line.substring(7).trim();
                        loadTexture(current, dir, map);
                    }
                } else if(line.startsWith("map_Ka ")) {
                    if(current!=null && !current.hasTexture()) {
                        String map=line.substring(7).trim();
                        loadTexture(current, dir, map);
                    }
                }
            }
        } catch(IOException|NumberFormatException e) {
            LoggerUtil.error("MtlReader: "+e.getMessage());
        }
        return materials;
    }

    private static void loadTexture(Material mat, String dir, String map) {
        File texFile = new File(dir, map);
        if(texFile.exists()) {
            try {
                Image img = new Image(new FileInputStream(texFile));
                mat.setTexture(img);
                LoggerUtil.info("MtlReader: Loaded texture "+texFile.getName());
            } catch(IOException e) {
                LoggerUtil.error("MtlReader: Failed to load texture "+texFile.getName()+": "+e.getMessage());
            }
        } else {
            LoggerUtil.warn("MtlReader: Texture file not found: "+texFile.getAbsolutePath());
        }
    }
}
