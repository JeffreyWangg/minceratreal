import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.joml.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImage.*;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.lang.Math;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameObject {
    private int x;
    private int y;
    private int z;
    private float vertices[];
    private int indices[];
    private float textures[];
    private int vertexCount;
    private Matrix4f model;

    public GameObject(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    public GameObject(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
        //we have to define vertex count
        createModelMatrixTranslate();
    }
    public void createModelMatrixTranslate(){
        this.model = new Matrix4f();
        model.translation(x, y, z);
    }
    public void cube(){
        this.vertices = new float[]{
                //assuming center of cube (x, y, z) is the origin (0, 0, 0)
                0.0f, 0.0f, 1.0f, // (-0.5, -0.5, 0.5) 0
                0.0f, 0.0f, 0.0f, // (-0.5, -0.5, -0.5) 1
                1.0f, 0.0f, 1.0f, // (0.5, -0.5, 0.5) 2
                1.0f, 0.0f, 0.0f, // (0.5, -0.5, -0.5) 3

                0.0f, 1.0f, 1.0f, // (-0.5, 0.5, 0.5) 4
                0.0f, 1.0f, 0.0f, // (-0.5, 0.5, -0.5) 5
                1.0f, 1.0f, 1.0f, // (0.5, 0.5, 0.5) 6
                1.0f, 1.0f, 0.0f, // (0.5, 0.5, -0.5) 7

                //duplicating for textures
                //front face
                0.0f, 0.0f, 1.0f, // (-0.5, -0.5, 0.5) 0 (8)
                0.0f, 1.0f, 1.0f, // (-0.5, 0.5, 0.5) 4 (9)
                1.0f, 0.0f, 1.0f, // (0.5, -0.5, 0.5) 2 (10)
                1.0f, 1.0f, 1.0f, // (0.5, 0.5, 0.5) 6 (11)
                //left face
                0.0f, 0.0f, 0.0f, // (-0.5, -0.5, -0.5) 1 (12)
                0.0f, 1.0f, 0.0f, // (-0.5, 0.5, -0.5) 5 (13)
                0.0f, 0.0f, 1.0f, // (-0.5, -0.5, 0.5) 0 (14)
                0.0f, 1.0f, 1.0f, // (-0.5, 0.5, 0.5) 4 (15)
                //right face
                1.0f, 0.0f, 1.0f, // (0.5, -0.5, 0.5) 2  (16)
                1.0f, 1.0f, 1.0f, // (0.5, 0.5, 0.5) 6  (17)
                1.0f, 0.0f, 0.0f, // (0.5, -0.5, -0.5) 3  (18)
                1.0f, 1.0f, 0.0f, // (0.5, 0.5, -0.5) 7  (19)
                //back face
                1.0f, 0.0f, 0.0f, // (0.5, -0.5, -0.5) 3  (20)
                1.0f, 1.0f, 0.0f, // (0.5, 0.5, -0.5) 7  (21)
                0.0f, 1.0f, 0.0f, // (-0.5, 0.5, -0.5) 5  (22)
                //NOTE: USE 1 FOR THE BACK FACE IN INDICES
        };

        this.indices = new int[]{
                0, 3, 1, 3, 2, 0, //bottom face
                8, 11, 9, 11, 10, 8, //front face
                12, 15, 13, 15, 14, 12, //left face
                20, 22, 21, 22, 1, 20, //back face
                16, 19, 17, 19, 18, 16, //right face
                4, 7, 5, 7, 6, 4  //top face
        };

        this.textures = new float[]{
                //bottom face
                0.5f, 0.0f, //0
                0.5f, 0.5f, //1
                1.0f, 0.0f, //2
                1.0f, 0.5f, //3
                //top face
                0.0f, 1.0f, //4
                0.0f, 0.5f, //5
                0.5f, 1.0f, //6
                0.5f, 0.5f, //7
                //front face
                0.0f, 0.5f, //0
                0.0f, 0.0f, //4
                0.5f, 0.5f, //2
                0.5f, 0.0f, //6
                //left face
                0.0f, 0.5f, //1
                0.0f, 0.0f, //5
                0.5f, 0.5f, //0
                0.5f, 0.0f, //4
                //right face
                0.0f, 0.5f, //2
                0.0f, 0.0f, //6
                0.5f, 0.5f, //3
                0.5f, 0.0f, //7
                //back face
                0.0f, 0.5f, //3
                0.0f, 0.0f, //7
                0.5f, 0.0f, //5

        };

        vertexCount = this.indices.length;
    }

//    public void render(){
//        glBindVertexArray(vao);
//        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
//        //  glBindBuffer(0);
//      //  glBindVertexArray(0);
//    }

    public Matrix4f getModel() {
        return model;
    }
    public int[] getIndices(){
        return indices;
    }
    public float[] getVertices(){
        return vertices;
    }
    public float[] getTextures() {
        return textures;
    }
    public int getVertexCount(){
        return vertexCount;
    }
    //    public boolean checkBoundingBox(Vector3f pos){
//        float xmin = vertices[0];
//        float xmax = vertices[6];
//        float ymin = vertices[1];
//        float ymax = vertices[13];
//        float zmin = vertices[5];
//        float zmax = vertices[2];
//
//        System.out.println(pos);
//        System.out.println("x:" + xmin);
//        System.out.println("x:" +xmax);
//        System.out.println("y:" +ymin);
//        System.out.println("y:" +ymax);
//        System.out.println("z:" +ymin);
//        System.out.println("z:" +ymax);
//
//
//
//        return (pos.x < xmax && pos.x > xmin) || (pos.y < ymax && pos.y > ymin) || (pos.z < zmax && pos.z > zmin);
//    }
}

//theory: there is only one set of vertices
//the model matrix is what adds multiple objects
//ADD TRANSFORMATION METHODS TO CHANGE MODEL MATRIX
//ADD RENDER METHOD TO EACH INDIVIDUAL CUBE

//gameobject vs mesh vs instanced mesh
//gameobject is positions
//mesh is the vbo and vao
//instanced mesh is the fancy mesh that drops everything at once

//okay so the java tutorial fucking sucks
//all i need to do is create the transformation array, bind a single instance of the cube, and draw it out
//lets try with the change cube model first and then try using the simple transformation
