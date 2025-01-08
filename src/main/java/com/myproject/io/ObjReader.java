package com.myproject.io;

import com.myproject.common.LoggerUtil;
import com.myproject.model.Material;
import com.myproject.model.Model;
import com.myproject.model.Polygon;
import com.myproject.model.Vertex;
import com.myproject.scene.SceneModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ObjReader {
    static class FaceVert {
        int v;
        int t;
        int n;
        FaceVert(int v,int t,int n){
            this.v=v;this.t=t;this.n=n;
        }
    }

    public static Model read(File file) {
        Model m = new Model();
        if (file == null || !file.exists()) {
            LoggerUtil.error("ObjReader: file doesn't exist");
            return m;
        }

        Map<String, Material> materials = new HashMap<>();
        String currentMtl=null;

        List<float[]> verts = new ArrayList<>();
        List<float[]> texcoords = new ArrayList<>();
        List<float[]> norms = new ArrayList<>();

        List<int[]> faces = new ArrayList<>();
        List<int[]> texFaces = new ArrayList<>();
        List<int[]> normFaces = new ArrayList<>();

        File mtlFile = null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String dir = file.getParent();
            String line;
            while((line=br.readLine())!=null) {
                line=line.trim();
                if(line.isEmpty()||line.startsWith("#")) continue;
                if(line.startsWith("v ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length<4) continue;
                    float x=Float.parseFloat(parts[1]);
                    float y=Float.parseFloat(parts[2]);
                    float z=Float.parseFloat(parts[3]);
                    verts.add(new float[]{x,y,z});
                } else if(line.startsWith("vt ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length<3) continue;
                    float u=Float.parseFloat(parts[1]);
                    float v=Float.parseFloat(parts[2]);
                    texcoords.add(new float[]{u,v});
                } else if(line.startsWith("vn ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length<4) continue;
                    float nx=Float.parseFloat(parts[1]);
                    float ny=Float.parseFloat(parts[2]);
                    float nz=Float.parseFloat(parts[3]);
                    norms.add(new float[]{nx,ny,nz});
                } else if (line.startsWith("f ")) {
                    String[] parts=line.split("\\s+");
                    if(parts.length<4) continue;
                    List<FaceVert> fvs = new ArrayList<>();
                    for(int i=1;i<parts.length;i++){
                        String[] sub=parts[i].split("/");
                        int vi = Integer.parseInt(sub[0])-1;
                        int ti = -1;
                        int ni = -1;
                        if(sub.length>1 && !sub[1].isEmpty()) ti=Integer.parseInt(sub[1])-1;
                        if(sub.length>2 && !sub[2].isEmpty()) ni=Integer.parseInt(sub[2])-1;
                        fvs.add(new FaceVert(vi,ti,ni));
                    }
                    for(int start=1;start<fvs.size()-1;start++){
                        FaceVert fv0=fvs.get(0);
                        FaceVert fv1=fvs.get(start);
                        FaceVert fv2=fvs.get(start+1);

                        faces.add(new int[]{fv0.v,fv1.v,fv2.v});
                        texFaces.add(new int[]{fv0.t,fv1.t,fv2.t});
                        normFaces.add(new int[]{fv0.n,fv1.n,fv2.n});
                    }
                } else if(line.startsWith("mtllib ")) {
                    String mtlName=line.substring(7).trim();
                    mtlFile = new File(dir, mtlName);
                    LoggerUtil.info("ObjReader: Found mtllib "+mtlName);
                } else if(line.startsWith("usemtl ")) {
                    currentMtl=line.substring(7).trim();
                    LoggerUtil.info("ObjReader: usemtl " + currentMtl);
                }
            }
        } catch (IOException|NumberFormatException e) {
            LoggerUtil.error("ObjReader: " + e.getMessage());
        }

        if(mtlFile!=null && mtlFile.exists()){
            materials = MtlReader.read(mtlFile);
        } else {
            LoggerUtil.warn("ObjReader: No MTL file found or mtllib not set.");
        }

        for(float[] v: verts) {
            m.addVertex(new Vertex(v[0],v[1],v[2]));
        }

        m.setTexCoords(texcoords);
        m.setNorms(norms);
        m.setTexFaces(texFaces);
        m.setNormFaces(normFaces);

        for(int[] f:faces){
            m.addPolygon(new Polygon(Arrays.asList(f[0],f[1],f[2])));
        }

        if(currentMtl!=null) {
            if(materials.containsKey(currentMtl)) {
                m.setMaterial(materials.get(currentMtl));
            } else {
                LoggerUtil.warn("ObjReader: Material "+currentMtl+" not found, using default diffuse color.");
                m.getMaterial().setDiffuse(javafx.scene.paint.Color.color(0.8,0.7,0.6));
            }
        } else {
            LoggerUtil.warn("ObjReader: No usemtl found, using default gray color.");
        }

        return m;
    }

    public static SceneModel createSceneModel(File file) {
        Model model = read(file);
        SceneModel sm = new SceneModel(model);
        sm.setName(file.getName());
        return sm;
    }
}
