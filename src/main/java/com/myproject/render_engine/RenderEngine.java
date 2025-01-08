package com.myproject.render_engine;

import com.myproject.scene.SceneModel;
import com.myproject.model.Model;
import com.myproject.model.Polygon;
import com.myproject.model.Vertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import com.myproject.math.MathUtils;
import com.myproject.math.Matrix4f;

import java.util.List;

public class RenderEngine {
    private Camera camera;
    private Rasterizer rasterizer;
    private boolean wireframe = false;

    public RenderEngine(Camera camera,int width,int height) {
        this.camera = camera;
        this.rasterizer = new Rasterizer(width,height);
    }

    public void setWireframe(boolean wf) {
        this.wireframe=wf;
    }

    public void render(Canvas canvas, List<SceneModel> models) {
        if (canvas==null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        PixelWriter pw = gc.getPixelWriter();
        rasterizer.clearZBuffer();

        Matrix4f view = camera.getViewMatrix();
        Matrix4f proj = camera.getProjectionMatrix();

        int w=(int)canvas.getWidth();
        int h=(int)canvas.getHeight();

        for (SceneModel sm: models) {
            if(!sm.isActive()) continue;
            Model m = sm.getModel();
            if (m.getVertices().isEmpty()||m.getPolygons().isEmpty()) continue;

            Matrix4f modelMat = sm.getTransformMatrix();
            Matrix4f mvp = Matrix4f.multiply(proj,Matrix4f.multiply(view,modelMat));
            List<Vertex> verts = m.getVertices();
            float[][] transformedVerts = new float[verts.size()][4];

            for(int i=0;i<verts.size();i++){
                Vertex v=verts.get(i);
                float[] res = MathUtils.multiplyMatrixByVector(mvp,v.getX(),v.getY(),v.getZ());
                transformedVerts[i][0]=res[0]/res[3];
                transformedVerts[i][1]=res[1]/res[3];
                transformedVerts[i][2]=res[2]/res[3];
                transformedVerts[i][3]=res[3];
                transformedVerts[i][0]=(transformedVerts[i][0]*0.5f+0.5f)*w;
                transformedVerts[i][1]=(1-(transformedVerts[i][1]*0.5f+0.5f))*h;
            }

            Color diffuse = m.getMaterial().getDiffuse();
            Color ambient = m.getMaterial().getAmbient();
            Color c = diffuse.interpolate(ambient,0.3);
            Image tex = m.getMaterial().getTexture();
            boolean hasTex = m.getMaterial().hasTexture();

            for (Polygon p: m.getPolygons()) {
                List<Integer> idx = p.getVertexIndices();
                if (!p.isValid()) continue;

                int i1=idx.get(0);
                int i2=idx.get(1);
                int i3=idx.get(2);

                float x1=transformedVerts[i1][0], y1=transformedVerts[i1][1], z1=transformedVerts[i1][2];
                float x2=transformedVerts[i2][0], y2=transformedVerts[i2][1], z2=transformedVerts[i2][2];
                float x3=transformedVerts[i3][0], y3=transformedVerts[i3][1], z3=transformedVerts[i3][2];

                float[][] tri = {
                        {x1,y1,z1},
                        {x2,y2,z2},
                        {x3,y3,z3}
                };

                if (wireframe) {
                    rasterizer.drawLine(pw,(int)x1,(int)y1,(int)x2,(int)y2,Color.WHITE,(z1+z2)*0.5);
                    rasterizer.drawLine(pw,(int)x2,(int)y2,(int)x3,(int)y3,Color.WHITE,(z2+z3)*0.5);
                    rasterizer.drawLine(pw,(int)x3,(int)y3,(int)x1,(int)y1,Color.WHITE,(z3+z1)*0.5);
                } else {
                    rasterizer.fillTriangle(pw, tri, c);
                }
            }
        }
    }
}
