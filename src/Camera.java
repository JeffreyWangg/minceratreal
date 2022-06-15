import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    private Matrix4f view = new Matrix4f();
    private Vector3f cameraPos = new Vector3f(0.0f, 100.0f,  0.0f);
    private Vector3f target = new Vector3f(0.0f, 0.0f, 0.0f);
    private Vector3f cameraUp = new Vector3f(0.0f, 1.0f,  0.0f);
    private Vector3f cameraDirection = new Vector3f(0.0f, 0.0f, -1.0f);
    private float yaw = -90.0f;
    private float pitch = 0.1f;
    private float lastX = 400;
    private float lastY = 300;
    private static boolean firstMouse = true;
    private boolean spaceWasPressed = false; //change THIS to something less stupid
    private float totalTime = 0.0f;
    private Vector3f startPos;
    private Vector3f deltaPos;


    public Camera(){
        view.lookAt(cameraPos, target, cameraUp);
        cameraDirection.set(0.0f, 0.0f, -1.0f);
    }

    public GLFWCursorPosCallbackI mouse_callback = new GLFWCursorPosCallbackI() {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            if (firstMouse) // initially set to true
            {
                lastX = (float)xpos;
                lastY = (float)ypos;
                firstMouse = false;
            }

            float xoffset = (float)xpos - lastX;
            float yoffset = lastY - (float)ypos; // reversed since y-coordinates range from bottom to top
            lastX = (float)xpos;
            lastY = (float)ypos;

            float sensitivity = 0.3f;
            xoffset *= sensitivity;
            yoffset *= sensitivity;

            yaw +=xoffset;
            pitch +=yoffset;
            if(pitch > 89.0f){
                pitch =  89.0f;}
            if(pitch < -89.0f) {
                pitch = -89.0f;
            }
            //         System.out.println("y offset:" + yoffset);
            //        System.out.println("pitch:" + pitch);
        }
    };
    public void setDirection(){
        //            cameraPos.sub(target, cameraDirection);
//            cameraDirection.normalize();
       // System.out.println(pitch);
        cameraDirection.x = (float)(Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        cameraDirection.y = (float)(Math.sin(Math.toRadians(pitch)));
        cameraDirection.z = (float)(Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
//            cameraDirection.normalize();
    }
    public void setDirection(float x, float y, float z) {
        cameraDirection.x = x;
        cameraDirection.y = y;
        cameraDirection.z = z;
    }
    public void setCameraPos(float x, float y, float z){
        cameraPos.x = x;
        cameraPos.y = y;
        cameraPos.z = z;
    }
    public void setCameraPosY(float y){
        cameraPos.y = y;
    }
    public void processInput(long window, boolean colliding, float dt){ //this should take in a force and return changes to said force
        float cameraSpeed = 10.0f;
        Vector3f sideVec = new Vector3f();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
//            cameraDirection.set(cameraDirection.x, 0.0f, cameraDirection.z);
            cameraPos.add(cameraDirection.x * cameraSpeed, 0.0f, cameraDirection.z * cameraSpeed);
           // cameraDirection.mul(cameraSpeed)
            //    System.out.println(cameraPos);
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            cameraPos.sub(cameraDirection.x * cameraSpeed, 0.0f, cameraDirection.z * cameraSpeed);
            //   System.out.println(cameraPos);
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            cameraDirection.cross(cameraUp, sideVec);
            sideVec.normalize().mul(cameraSpeed);
            cameraPos.sub(sideVec);

        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            cameraDirection.cross(cameraUp, sideVec);
            sideVec.normalize().mul(cameraSpeed);
            cameraPos.add(sideVec);
        }

        if(colliding && !spaceWasPressed) {
            if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
                System.out.println("a");
                totalTime = 0.0f;
                startPos = new Vector3f(cameraPos.x, cameraPos.y, cameraPos.z);
                deltaPos = new Vector3f();

                setCameraPosY(cameraPos.y + 0.01f);
                //when spacebar is pressed, apply negative force to player
                //so player goes upward with jerk of 1m/s^3
                //which means acceleration =
                cameraPos.sub(startPos, deltaPos);
                spaceWasPressed = true;
                System.out.println("start");
            }
        } else if (!colliding && spaceWasPressed){
            totalTime += dt;
            cameraPos.sub(startPos, deltaPos);
          //  System.out.println(yPosition(dt));

            setCameraPosY(yPosition(dt)); //peak height = 1.5f

        } else if(colliding && spaceWasPressed){
            System.out.println("end");
            spaceWasPressed = false;
        }
     //   System.out.println(cameraPos);
    }
    public float yPosition(float dt){
//        System.out.println(startPos.y);
//        System.out.println("1st: " + (-1.5f * 0.1f * 0.1f) * totalTime * totalTime);
//        System.out.println("2nd: " + ((2 * 1.5f * 0.1f) * totalTime + startPos.y));

        System.out.println((-1.5f * 0.1f * 0.1f) * totalTime * totalTime + (2 * 1.5f * 0.1f) * totalTime + startPos.y);
        return (-1.5f * 0.1f * 0.1f) * totalTime * totalTime + (2 * 1.5f * 0.1f) * totalTime + startPos.y;
    }

    public boolean isSpaceWasPressed() {
        return spaceWasPressed;
    }

    public void processInputFree(long window){ //this should take in a force and return changes to said force
        float cameraSpeed = 0.1f;
        Vector3f sideVec = new Vector3f();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            cameraPos.add(cameraDirection.mul(cameraSpeed));
            //    System.out.println(cameraPos);
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
            cameraPos.sub(cameraDirection.mul(cameraSpeed));
            //   System.out.println(cameraPos);
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            cameraDirection.cross(cameraUp, sideVec);
            cameraPos.sub(sideVec.mul(cameraSpeed));
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            cameraDirection.cross(cameraUp, sideVec);
//                System.out.println("direction" + cameraDirection);
//                System.out.println("side" + sideVec);
            cameraPos.add(sideVec.mul(cameraSpeed));
        }
//        if(glfwGetKey(window, GLFW_KEY_Q) == GLFW_PRESS){
//            cameraDirection.y = (float)(Math.sin(Math.toRadians(10)));
//        }
    }
    public void changeDirection(long window, String mode, boolean colliding, float dt){
//        view.setLookAt(cameraPos, target, cameraUp);
        if(mode.equals("free")) {
            processInputFree(window);
        } else {
            processInput(window, colliding, dt);
        }
        cameraPos.add(cameraDirection,target);
        view.setLookAt(cameraPos, target, cameraUp);
    }
    public Matrix4f getView(){
        return view;
    }

    public Vector3f getPos() {return cameraPos;}

    public Vector2f getPlayerChunk(){
        int playerx = ((int)Math.floor(cameraPos.x) - ((int)Math.floor(cameraPos.x) & 15));
        int playerz = ((int)Math.floor(cameraPos.z) - ((int)Math.floor(cameraPos.z) & 15));

        return new Vector2f(playerx, playerz);
    }
    public Vector3f getCameraDirection(){
        return cameraDirection;
    }
}

//yaw is horizontal rotation (speen on the plane of x and z)
//pitch is vertical rotation (flips around the x axis)
//we dont care about roll
//given pitch and yaw we can create a new vector using trig



//if collision get direciton judge direction of collision and pushback only against direction of collision

//private start time
//priate total time
//private lateralspeed
//plugin deltatime into function
//total time += deltatime
//lateral speed = dpos/dt
//pos(totaltime, lateralspeed)
//peak height = 1.5f
//if player not colliding
//continue pos function
//else just be normal

//our position function = (-(peak height)(lateral Speed^2)) t^2 + (2(peak height)lateral Speed)t + p0
