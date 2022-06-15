import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL30.*;

public class ShaderProgram {
    private int programId;

    private int vertexShaderId;

    private int fragmentShaderId;
    private static String vertex_shader = "#version 330 core\n"+
            "layout (location = 0) in vec3 aPos;\n"+
            "layout (location = 1) in vec2 aTexCoord;"+
            "layout (location = 2) in mat4 aModel;" +
            "uniform mat4 view;"+
            "uniform mat4 projection;"+
            "out vec2 TexCoord;" +
            "out float aPosy;"+
            "void main()\n"+
            "{\n"+
            "   gl_Position = projection * view * aModel * vec4(aPos, 1.0);\n"+
       //     "aPosy = aPos.y;"+
         //   "   gl_Position = vec4(aPos, 1.0);\n"+
            "TexCoord = aTexCoord;"+
            "}";

    private static String frag_shader =
            "#version 330 core\n"+
            "out vec4 FragColor;\n"+

            "uniform vec3 ourColor;\n"+
            "in vec2 TexCoord;\n"+
            "uniform sampler2D texture1;\n"+

            "void main()\n"+
            "{\n"+
        //    "FragColor = texture(texture1, TexCoord);\n"+
     //          "vec3 newY = vec3(aPosy * 40.0f, aPosy * 40.0f, aPosy * 40.0f);"+
//                    "if(aPosy < 0){ " +
//                    "vec3 newY = vec3(255.0f, 255.0f, 255.0f);"+
//                "FragColor = vec4(newY, 1.0);\n"+
               "FragColor = texture(texture1, TexCoord);\n"+
            "}\n";


    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        createVertexShader(vertex_shader);
        createFragmentShader(frag_shader);
        link();
    }

    public void setUniform4f(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            int loc = glGetUniformLocation(programId, uniformName);
            glUniformMatrix4fv(loc, false, fb);

//            if(fb != null){
//                MemoryUtil.memFree(fb);
//            }
        }
    }
//    public void setUniform(String uniformName, int value) {
//        // Dump the matrix into a float buffer
//        int loc = glGetUniformLocation(programId, uniformName);
//        glUniformMatrix4fv(loc, false, fb);
//        shaderProgram.bind();
//        glUniform1i(glGetUniformLocation(shaderProgram.getProgramId(), "texture1"), 1);
//    }

//    public setUniform(){
//
//    }

    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
        glDeleteShader(vertexShaderId);
        glDeleteShader(fragmentShaderId);
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    public int getProgramId(){
        return programId;
    }


}
