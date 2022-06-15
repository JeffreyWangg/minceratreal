import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {
    protected int vao;
    protected ArrayList<Integer> vboList;
    private int vertexCount;

    public Mesh(float[] vertices, float[] textures, int[] indices){
        vao = glGenVertexArrays();
        vboList = new ArrayList<>();

        int vertexVbo = glGenBuffers();
        int textureVbo = glGenBuffers();
        int ebo = glGenBuffers();

        glBindVertexArray(vao);
        //binding vertex buffer to gl_array_buffer target
        //vbo that attributes are taken from are determined by the bound vbo to array buffer

        //vertex vbo population
        glBindBuffer(GL_ARRAY_BUFFER, vertexVbo);
        //static_draw = set once, used many times
        //copies user data into targeted buffer
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
        //data is now stored as array_buffer in gpu memory
        vboList.add(vertexVbo);

        glEnableVertexAttribArray(0);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0); //4 = sizeof float

        //texture vbo population
        glBindBuffer(GL_ARRAY_BUFFER, textureVbo);
        glBufferData(GL_ARRAY_BUFFER, textures, GL_STATIC_DRAW);
        vboList.add(textureVbo);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0);

        //ebo binding + population
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        vboList.add(ebo);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        vertexCount = indices.length;
    }
    public void renderMesh(){
        glBindVertexArray(vao);
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
    }

}
