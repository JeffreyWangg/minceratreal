import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Vector;

import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public class Chunk {
    private float[] modelMatrixList;
    private int initialx;
    private int initialz;
    private Vector3f[] chunkBlocks;
    public Chunk(int x, int z){
        initialx = x;
        initialz = z;
        populateChunk();
    }

    public void populateChunk(){
    //    ArrayList<GameObject> cubes = new ArrayList<>();
        Noise noiseFunc = new Noise(256, 69);
        float[] heightMap = noiseFunc.create3D(16, 16, initialx, initialz);
        modelMatrixList = new float[16 * 16 * 16];
        chunkBlocks = new Vector3f[16 * 16];

        for(int i = 0; i < 16; i++){
            for(int j = 0; j < 16; j++){
                int x = initialx + j;
//                float y = mapData.get((j + mapWidth[0] * i));
                int y = (int)Math.floor(heightMap[i * 16 + j] * 15);
                int z = initialz + i;

                Matrix4f model = new Matrix4f().translate(x, y, z);
                model.get(modelMatrixList, 16 * (i * 16 + j)); //size of each matrix is 4 * 4 = 16 values
                chunkBlocks[i * 16 + j] = new Vector3f(x, y, z);
                //16 * 16 cubes with 16 values in each matrix
                //add translated matrices to list
            }
        }
//        chunkBlocks = new GameObject[cubes.size()];
//        int count = 0;
//        for (GameObject cube : cubes) {
//            chunkBlocks[count++] = (cube != null ? cube : new GameObject()); // Or whatever default you want.
//        }
    }
    public boolean checkCollision(Vector3f pos){
        int queryx = (int)Math.floor(pos.x) - initialx;
        int queryz = (int)Math.floor(pos.z) - initialz;
 //       System.out.println(queryx + " " + queryz);
        Vector3f block = chunkBlocks[queryz * 16 + queryx];
//        System.out.println("x: " + pos.x + " vs " + block.x);
//        System.out.println(pos.x < block.x + 0.5f && pos.x > block.x - 0.5f);
//        System.out.println("y: " + (pos.y - 2.0f) + " vs " + block.y);
//        System.out.println(pos.y < block.y + 0.5f && pos.y > block.y - 0.5f);
//        System.out.println("z: " + pos.z + " vs " + block.z);
//        System.out.println(pos.z < block.z + 0.5f && pos.z > block.z - 0.5f);
        if((pos.x <= block.x + 1.0f && pos.x >= block.x) &&
            (pos.y - 3.0f <= block.y && pos.y - 3.0f >= block.y - 1.0f) &&
            (pos.z <= block.z + 1.0f && pos.z >= block.z))
        {
            return true;
        }
        return false;
    }
    public Vector3f getBlock(Vector3f pos){
        int queryx = (int)Math.floor(pos.x) - initialx;
        int queryz = (int)Math.floor(pos.z) - initialz;
        return chunkBlocks[queryz * 16 + queryx];
    }
    public float[] getModelMatrixList(){
        return modelMatrixList;
    }
    public Vector2f getInitPos(){
        return new Vector2f(initialx, initialz);
    }
    public String toString(){
        return "chunk at " + getInitPos();
    }
}
//chunk distance of 1 means we want to create 1 chunk square around the player

//ok so we use the model matrix to apply transformations to all objects
//study the object instance drawing method
//rewrite the model matrix into an ARRAY of model matrices
//write data to model matrix vbo
//method translationRotateScale


//gameobject stores positions
//each gameobject has positions
//we take all gameobjects (float array or something) and put into chunk class
//gameobjects will all be the same cubes at this point
//each cube will get a model matrix that
//chunk class will use instanced drawing
//will have to define model matrix for every instance

//i have noclue what to do
//maybe create a cube vao???
//so we have a single gameobject cube that we assign a vao to
//oh but at the same time we have other cubes that we want to render too right
//do we render different textures seperately????? how does that work???
//rn lets keep the cube class but instead just have the one cube with

//grab the vertices from the gameobject and have them inside this instance class
//then we can bind vbo with indices etc?????
//then add instance mesh
//god i dont know this is a goddamn mess
//no we have the cube instance already so yes we create a cube and we access everything from the cube

//if we wanted two seperate blocks in one chunk how would we do that mesh wise
//maybe i should just give blocks a tag




