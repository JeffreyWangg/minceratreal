//import org.joml.Matrix4f;
//
//import static org.lwjgl.opengl.GL11.GL_FLOAT;
//import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
//import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
//import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
//import static org.lwjgl.opengl.GL30.glBindVertexArray;
//import static org.lwjgl.opengl.GL30.glGenVertexArrays;
//import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
//import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
//
//public class temp {
//    import org.joml.Matrix4f;
//
//import java.util.ArrayList;
//
//import static org.lwjgl.opengl.GL11.GL_FLOAT;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
//import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
//import static org.lwjgl.opengl.GL30.glBindVertexArray;
//import static org.lwjgl.opengl.GL30.glGenVertexArrays;
//import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
//import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;
//
//    public class Chunk {
//        private int initialx;
//        private int initialz;
//        private GameObject[] chunkBlocks;
//        private float[] modelMatrixList;
//        private int vao;
//        private GameObject cube = new GameObject();
//        public Chunk(int x, int z){
//            initialx = x;
//            initialz = z;
//            cube.cube();
//            vao = glGenVertexArrays();
//            populateChunk();
//            createMesh();
//            addInstanceMesh();
//        }
//
//        public void populateChunk(){
//            //    ArrayList<GameObject> cubes = new ArrayList<>();
//            Noise noiseFunc = new Noise(256, 69);
//            float[] heightMap = noiseFunc.create3D(16, 16, initialx, initialz);
//            modelMatrixList = new float[16 * 16 * 16];
//
//            for(int i = initialz; i < initialz + 16; i++){
//                for(int j = initialx; j < initialx + 16; j++){
//                    int x = -8 + j;
////                float y = mapData.get((j + mapWidth[0] * i));
//                    int y = (int)Math.floor(heightMap[(i - initialz) * 16 + (j-initialx)] * 15);
//                    int z = -8 + i;
//
//                    Matrix4f model = new Matrix4f().translate(x, y, z);
//                    model.get(modelMatrixList, 16 * ((i - initialz) * 16 + (j - initialx))); //size of each matrix is 4 * 4 = 16 values
//                    //16 * 16 cubes with 16 values in each matrix
//
//                    //add translated matrices to list
//                }
//            }
//
////        chunkBlocks = new GameObject[cubes.size()];
////        int count = 0;
////        for (GameObject cube : cubes) {
////            chunkBlocks[count++] = (cube != null ? cube : new GameObject()); // Or whatever default you want.
////        }
//        }
//        public void createMesh(){
//            int vertexVbo = glGenBuffers();
//            int textureVbo = glGenBuffers();
//            int ebo = glGenBuffers();
//
//            glBindVertexArray(vao);
//            //binding vertex buffer to gl_array_buffer target
//            //vbo that attributes are taken from are determined by the bound vbo to array buffer
//
//            //vertex vbo population
//            glBindBuffer(GL_ARRAY_BUFFER, vertexVbo);
//            //static_draw = set once, used many times
//            //copies user data into targeted buffer
//            glBufferData(GL_ARRAY_BUFFER, cube.getVertices(), GL_STATIC_DRAW);
//            //data is now stored as array_buffer in gpu memory
//
//            glEnableVertexAttribArray(0);
//            glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * 4, 0); //4 = sizeof float
//
//            //texture vbo population
//            glBindBuffer(GL_ARRAY_BUFFER, textureVbo);
//            glBufferData(GL_ARRAY_BUFFER, cube.getTextures(), GL_STATIC_DRAW);
//            glEnableVertexAttribArray(1);
//            glVertexAttribPointer(1, 2, GL_FLOAT, false, 2 * 4, 0);
//
//            //ebo binding + population
//            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
//            glBufferData(GL_ELEMENT_ARRAY_BUFFER, cube.getIndices(), GL_STATIC_DRAW);
//
//            glBindBuffer(GL_ARRAY_BUFFER, 0);
//            glBindVertexArray(0);
//        }
//
//        public void addInstanceMesh(){
//            int instanceVbo = glGenBuffers();
//
//            glBindVertexArray(vao);
//            glBindBuffer(GL_ARRAY_BUFFER, instanceVbo);
//            glBufferData(GL_ARRAY_BUFFER, modelMatrixList, GL_STATIC_DRAW);
//
//            for(int i = 0; i < 4; i++) {
//                glEnableVertexAttribArray(2 + i);
//                glVertexAttribPointer(2 + i, 4, GL_FLOAT, false, 64, i * 16);
//                glVertexAttribDivisor(2 + i, 1); // tell OpenGL this is an instanced vertex attribute.
//            }
//            glBindBuffer(GL_ARRAY_BUFFER, 0);
//            //unbind vbo and vao
//            glBindVertexArray(0);
//        }
//
//        public void render(){
//            glBindVertexArray(vao);
//            glDrawElementsInstanced(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0, 16*16);
//        }
//    }
////chunk distance of 1 means we want to create 1 chunk square around the player
//
////ok so we use the model matrix to apply transformations to all objects
////study the object instance drawing method
////rewrite the model matrix into an ARRAY of model matrices
////write data to model matrix vbo
////method translationRotateScale
//
//
////gameobject stores positions
////each gameobject has positions
////we take all gameobjects (float array or something) and put into chunk class
////gameobjects will all be the same cubes at this point
////each cube will get a model matrix that
////chunk class will use instanced drawing
////will have to define model matrix for every instance
//
////i have no fucking clue what to do
////maybe create a cube vao???
////so we have a single gameobject cube that we assign a vao to
////oh but at the same time we have other cubes that we want to render too right
////do we render different textures seperately????? how does that work???
////rn lets keep the cube class but instead just have the one cube with
//
////grab the vertices from the gameobject and have them inside this instance class
////then we can bind vbo with indices etc?????
////then add instance mesh
////god i dont fucking know this is a goddamn mess
////no we have the cube instance already so yes we create a cube and we access everything from the cube
//
////if we wanted two seperate blocks in one chunk how would we do that mesh wise
////maybe i should just give blocks a tag
//
//
//
//
//
//}
