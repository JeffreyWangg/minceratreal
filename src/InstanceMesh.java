import java.util.Arrays;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

public class InstanceMesh extends Mesh{
    private float[] modelMatrixList;
    private int numInstances;
    public InstanceMesh(int numInstances, float[] vertices, float[] textures, int[] indices, float[] modelList){
        super(vertices, textures, indices);
        this.numInstances = numInstances;
        this.modelMatrixList = modelList;
        addInstanceMesh();
    }
    public void addInstanceMesh(){
        int instanceVbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, instanceVbo);
        glBufferData(GL_ARRAY_BUFFER, modelMatrixList, GL_STATIC_DRAW);
        vboList.add(instanceVbo);

        for(int i = 0; i < 4; i++) {
            glEnableVertexAttribArray(2 + i);
            glVertexAttribPointer(2 + i, 4, GL_FLOAT, false, 64, i * 16);
            glVertexAttribDivisor(2 + i, 1); // tell OpenGL this is an instanced vertex attribute.
        }
        //unbind vbo and vao
        glBindVertexArray(0);
    }
    public void renderInstanceMesh(){
        glBindVertexArray(vao);
        glDrawElementsInstanced(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0, numInstances);
    }
    public int getVao(){
        return vao;
    }
}

//bind vao
//bind buffer for single instance (cube)
//bind buffer for instance vbo
//somehow make model matrices
//render?

//number of instances = one chunk
//we have the model matrix
//model matrix runs for all models
//so we just define the model matrix and then we just make many chunk thats that


