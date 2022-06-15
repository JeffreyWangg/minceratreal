import org.joml.Vector3f;

public class Force {
    private Vector3f force = new Vector3f();
    private float gravityAcceleration = 9.8f;
    public Force(float dt){
        force.x = 0.0f;
        force.y = gravity(0.0f, dt);
        force.z = 0.0f;
    }
    public void addForce(float x, float y, float z){
        force.add(x, y, z);
    }
    public float gravity(float velocity, float dt){
        return velocity - dt * gravityAcceleration;
    }
    public Vector3f getForce() {
        return force;
    }
}


//f(t) = 1/2gt^2 + v0t + p0
//f(t) = position over time
//g = gravity, v0 = initial velocity, p0 = initial position, t = time
//get gravity + initial velocity from peak height and distance to peak

//v0 = -gt(h)
//duration to peak of jump defined in advance (change constant)
//t(h) = time at peak
//2t(h) = -v0

//assuming p0 = 0
//g = -2h/t(h)^2
//h = height at peak of jump

//lateral speed v(x)
//horizontal and vertical components should be kept seperate
//so we add vertical velocity on top of horizontal velocity

//t(h) will be a function of lateral speed and distance to peak
//t(h) = x(h)/v(x)
//g = -2hv(x)^2/x(h)^2
//v(0) = 2hv(x)/x(h)
//assuming lateral foot speed v(x) = cameraSpeed
//h = 1.5f
//x(h) = 1.0f;

//lateral speed = deltaPos/deltaTime
