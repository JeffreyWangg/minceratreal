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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {
    private static long window;
    private static GLFWKeyCallback esckeyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                glfwSetWindowShouldClose(window, true);
            }
        }
    };
    private static GLFWKeyCallback enterkeyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW_KEY_ENTER && action == GLFW_PRESS) {
                glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            }
        }
    };

    private static GLFWFramebufferSizeCallback framebuffer_size_callback = new GLFWFramebufferSizeCallback() {
        @Override
        public void invoke(long window, int width, int height) {
            glViewport(0, 0, width, height);
        }
    };

    public static void main(String[] args) throws Exception {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        window = glfwCreateWindow(800, 600, "LearnOpenGL", NULL, NULL);
        if (window == NULL) {
            glfwTerminate();
        }
        //????? wtf does this mean
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        int[] winHeight = new int[1];
        int[] winWidth = new int[1];
        glfwGetFramebufferSize(window, winWidth, winHeight);

        glViewport(0, 0, winWidth[0], winHeight[0]);
//        glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);
        glfwSetKeyCallback(window, esckeyCallback);
        //       glfwSetKeyCallback(window, enterkeyCallback);
        glfwSetFramebufferSizeCallback(window, framebuffer_size_callback);

        //shaders made in gsls, create and compile

        ShaderProgram shaderProgram = new ShaderProgram();
        //create shader program
        //link to shader program object and activate shader program when rendering
        //use with glUseProgram();

        //import texture
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);    // set texture wrapping to GL_REPEAT (default wrapping method)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        // set texture filtering parameters
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        int[] width = new int[1], height = new int[1], nrChannels = new int[1];
        ByteBuffer data = STBImage.stbi_load("/Users/jwang/IdeaProjects/testlwjgl/src/cube_texture.png", width, height, nrChannels, 4);
        //     data.flip();
        //    System.out.println(width[0] + " " + height[0]);
        //      glPixelStorei(GL_UNPACK_ALIGNMENT, 1); //add this???
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width[0], height[0], 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);
        //  System.out.println(data);
        STBImage.stbi_image_free(data);

        shaderProgram.bind();
        glUniform1i(glGetUniformLocation(shaderProgram.getProgramId(), "texture1"), 0);

//        int mapHeight = 128, mapWidth = 128;
//        Noise noiseFunc = new Noise(256, 212084);
//        float[] heightMap = noiseFunc.create3D(mapHeight, mapWidth);

        //for loop for coords of top left block of chunk
        //each chunk will span 16 blocks because thats cool

//        ArrayList<GameObject> cubes = new ArrayList<>();

        //unsigned char* is a pointer to the first element of the byte array
        //if i just copy the code example assuming that mapData.get(0) == unsigned char* maybe something will happen
//        for(int i = 0; i < mapHeight; i++) {
//            for(int j = 0; j < mapWidth; j++) {
//                //bytebuffer is incorrect!!!!!!! you are wrong!!!!
//                int x = -(mapWidth/2) + j;
////                float y = mapData.get((j + mapWidth[0] * i));
//                int y = (int)Math.floor(heightMap[i * mapWidth + j] * 15);
//                int z = -(mapHeight/2) + i;
//
//                GameObject cube = new GameObject(x, y, z);
//                cube.cube();
//                cube.bindBuffer();
//
//                cubes.add(cube);
//            }
//        }

        //take position
        //if position changes & 15 then updte chunks and center position around new position

        //but for some reason we want to inverse please look into why

//        Chunk[] chunkBlockArray = new Chunk[chunks.size()];
//        int count = 0;
//        for (Chunk chunk : chunks) {
//            chunkBlockArray[count++] = (chunk != null ? chunk : null); // Or whatever default you want.
//        }

        //rd of 2
        //x x x x x
        //x x x x x
        //x x O x x
        //x x x x x
        //x x x x x
        Camera camera = new Camera();
        GameObject cube = new GameObject();
        HashMap<Vector2f, InstanceMesh> vaos = new HashMap<>();
        HashMap<Vector2f, Chunk> chunks = new HashMap<>();
        cube.cube();
        int renderDistance = 16;
    //    InstanceMesh[] instances = generateInstancesfromPos(camera.getPlayerChunk(), renderDistance, cube);
//        InstanceMesh[] instances = new InstanceMesh[1];
//        Chunk chunk = new Chunk(-16, -16);
//        Chunk chunk1 = new Chunk(0, 0);
//        instances[0] = new InstanceMesh(256, cube.getVertices(), cube.getTextures(), cube.getIndices(), chunk.getModelMatrixList());
//        instances[0] = new InstanceMesh(256, cube.getVertices(), cube.getTextures(), cube.getIndices(), chunk1.getModelMatrixList());

        glEnable(GL_DEPTH_TEST);
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        glfwSetCursorPosCallback(window, camera.mouse_callback);

        //    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        float deltaTime = 0.0f;
        float lastFrame = 0.0f;
        Vector3f lastPos = camera.getPos();
        Vector3f deltaPos = new Vector3f(0.0f, 0.0f, 0.0f);

        float gravityaccel = 0.098f * 2; //after 1 second, velocity += 9.8 m/s
        float velocity = 0.0f; //velocity sits at 0.0 m/s while resting

        while (!glfwWindowShouldClose(window)) {
//            glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL_DEPTH_BUFFER_BIT);

            float currentFrame = (float)glfwGetTime(); //running at around 60 "fps"
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

//            System.out.println("position: " + camera.getPos());

        //    System.out.println("player chunk: " + camera.getPlayerChunk());
//            Vector3f camPos = camera.getPos();
//            if(cubeArray[(int)(Math.floor(camPos.x) * mapWidth + Math.floor(camPos.z))].checkBoundingBox(camPos)){
//                System.out.println("collision detected");
//            }
            Matrix4f projection = new Matrix4f().perspective((float) Math.toRadians(45.0f), 800.0f / 600.0f, 0.1f, 1000.0f);
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, texture);
            shaderProgram.bind();
            shaderProgram.setUniform4f("view", camera.getView());
            shaderProgram.setUniform4f("projection", projection);

            //slap this in a function
            //chunk loading:
            //this works by getting the chunk the player is in, and rendering all chunks around the central chunk
            //instance meshes are stored in a hashmap where the chunk position is the key
            //chunks are stored in a hashmap where the initial position is the key and the chunk itself is the value
            //^used for collision
            for (int i = (int) camera.getPlayerChunk().y / 16; i < (int) camera.getPlayerChunk().y / 16 + renderDistance * 2 + 1; i++) {
                for (int j = (int) camera.getPlayerChunk().x / 16; j < (int) camera.getPlayerChunk().x / 16 + renderDistance * 2 + 1; j++) {
                    Vector2f initpos = new Vector2f((j - renderDistance) * 16, (i - renderDistance) * 16);

                    if(vaos.containsKey(initpos)){
                        vaos.get(initpos).renderInstanceMesh();
                    } else {
                        Chunk chunk = new Chunk((j - renderDistance) * 16, (i - renderDistance) * 16);
                        InstanceMesh instance = new InstanceMesh(256, cube.getVertices(), cube.getTextures(), cube.getIndices(), chunk.getModelMatrixList());
                        vaos.put(chunk.getInitPos(), instance);
                        chunks.put(chunk.getInitPos(), chunk);
                        instance.renderInstanceMesh();
                    }
                    //            System.out.println("chunk at " + j + ", " + i + ": " + "(" + (j - renderDistance) * 16 + ", " + (i - renderDistance) * 16 + ")");
                }
            }
            //simply set player pos here as always moving down
            //below this is process input so that when spacebar hit, jump acceleration is applied to counter the downward force
         //   System.out.println("player pos: " + camera.getPos());
//            System.out.println("player chunk: " + camera.getPlayerChunk());

            Force force = new Force(deltaTime);

            lastPos = camera.getPos();

            camera.setDirection();
            //every frame we update the camera direction vector
            //add position to target so that the target moves with pos
            camera.changeDirection(window, "any", chunks.get(camera.getPlayerChunk()).checkCollision(camera.getPos()), deltaTime);

            camera.getPos().sub(lastPos, deltaPos);

            if(!chunks.get(camera.getPlayerChunk()).checkCollision(camera.getPos()) && !camera.isSpaceWasPressed()) {
              //  System.out.println("not colliding");
                Vector3f pos = camera.getPos();
                camera.setCameraPos(pos.x, pos.y +  force.getForce().y, pos.z);
                if (velocity != 9.8f) {
                    velocity += gravityaccel * deltaTime;
                } else {
                    velocity = 0.0f;
                }
            }
            else if(!camera.isSpaceWasPressed()){
                System.out.println("colliding and space not pressed");
                Vector3f block = chunks.get(camera.getPlayerChunk()).getBlock(camera.getPos());
                camera.setCameraPos(camera.getPos().x, block.y + 3.0f, camera.getPos().z);
                velocity = 0.0f;
            }
//            else {
//                camera.setCameraPosY(chunks.get(camera.getPlayerChunk()).getBlock(camera.getPos()).y);
//            }

            glBindVertexArray(0);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        glfwTerminate();
    }
}

//crashed at 16 96
//crashed at 16 208

//object class that allows the creation of more blocks
//figure out textures

//render and create chunks procedurally and store already calculated + rendered chunks

//if player is within x and z boundaries of a chunk then render around that chunk

//chunks end and start in center of squares; please fix

//vector is key, vao is value

//when the player walks into a bloc, i need to determine their direction vector and simply remove the direction vector of collision
//but then i also need to push the player backwards

//prevent tunneling - if there exists a block between point a and b, shift the player to that block

//1 newton = gives 1kg acceleration of 1m/s^2
//if there is a force of 1n, then the player should experience movement at 1m/s^2
//we can create a force class that calculates force and applies force to an entity's position
//so dvelocity = accleration * dt
//velocity = X m/s
//force = mass * acceleration
//
//so the player is 1kg lmao




