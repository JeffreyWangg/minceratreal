import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Random;
import java.util.Arrays;

//maybe combine this with chunks
//so do 16 step x value change
//every 16 divide by 16 to generate x values to interpolate
//maybe add to the rand table for every new chunk generated?

public class Noise {
    private int mask;
    private int size;
    private int permutationTable[];
    private Vector2f randomValues[];

    public Noise(int size, int seed){
        this.size = size;
        this.mask = size - 1;
        randomValues = new Vector2f[size];
        populateRandTable(seed);
    }

    public float[] create3D(int height, int width, int initialx, int initialz){
        permutationTable = new int[size * 2];
        populatePermTable2D(permutationTable);

        float[] perlinHeights = new float[height * width]; //height 16, width 16
        int numSteps = 2000;
        for(int i = initialz; i < initialz + height; i++){
            float x = i/(float)(numSteps - 1) * height;
            for(int j = initialx; j < initialx + width; j++){
                float y = j/(float)(numSteps - 1) * width;
                perlinHeights[(i - initialz)* width + (j-initialx)] = eval(x, y); //frequency can be added
            }
        }

        return perlinHeights;
    }

    public void populatePermTable2D(int[] perm){
        Random rand = new Random(1);
        for(int i = 0; i < size; i++){
            perm[i] = i;
            perm[i + size] = i;
        }
        for(int i = 0; i < size; i++){
            int random = rand.nextInt() & 255;
            int temp = perm[random];
            perm[random] = perm[i];
            perm[i] = temp;
            perm[i + size] = perm[i];
        }
    }
    public void populateRandTable(int seed){
        Random rand = new Random(seed);
        for(int i = 0; i < size; i++){
            randomValues[i] = new Vector2f(rand.nextFloat(), rand.nextFloat());
            //do length2 operation?
            randomValues[i].normalize();
        }
    }
    public Vector2f hash2D(int x, int y) {
        return randomValues[permutationTable[permutationTable[x] + y]];
    }

    public float eval(float x, float y){
        int xmin = (int)(Math.floor(x)) & mask;
        int ymin = (int)(Math.floor(y)) & mask;

        int xmax = (xmin + 1) & mask;
        int ymax = (ymin + 1) & mask;

        float tx = x - (int)(Math.floor(x));
        float ty = y - (int)(Math.floor(y));

        float sx = smoothstep(tx);
        float sy = smoothstep(ty);

        Vector2f v00 = hash2D(xmin, ymin);
        Vector2f v01 = hash2D(xmin, ymax);
        Vector2f v10 = hash2D(xmax, ymin);
        Vector2f v11 = hash2D(xmax, ymax);

        float negtx = tx - 1;
        float negty = ty - 1;

        Vector2f v00t = new Vector2f(tx, ty);
        Vector2f v01t = new Vector2f(tx, negty);
        Vector2f v10t = new Vector2f(negtx, ty);
        Vector2f v11t = new Vector2f(negtx, negty);

        float newx1 = lerp(v01.dot(v01t), v11.dot(v11t), sx);
        float newx2 = lerp(v00.dot(v00t), v10.dot(v10t), sx);

        return lerp(newx2, newx1, sy); //interpolating y to produce random terrain
    }

    public float smoothstep(float t){
        return t*t*t * (6*t*t - 15*t + 10);
    }

    public float lerp(float min, float max, float t){
    //    System.out.println(min * (1.0f - t) + max * t);
        return min * (1.0f - t) + max * t; //explain this pls
    }
}

//perlin noise
//want a function that is diferentiable and continuous to blur random values on lattice points on a table
//generate a 1d array of 256 directions and corners of cell randomly pickup direction from
//create multiple functions capable of being periodic??? or something???
//permutation table is sized 512 in perlin method
//input x value which turns into int is looked up in permutation table
//result of lookup is added with input y (int) which is looked up again to get the randomized noise

//noise function is input x [-infinity, infinity], come out with float between [0, 1]
//imagine you have a ruler and the x coordinate is the tick on the rule
//that is called the lattice matrix (1d)
//when x is not an integer, we used linear interpolation using the two closest defined x values on the lattice
//we need a value t between range [0, 1] to perform linear interpolation
//therefore, we take x and subtract (int)x
//we want to be able to interpolate on a range of array.length + 1
//to do this we use modulo in order to interpolate between array.length and 0, which allows us to create periodic noise
//if modulo array.length + 1, use 0 and array.length to interpolate
//using t directly SUCKS because it is sharp and not smooth
//we can remap t as S by using smoothstep or cosine functions
//we DO NOT change the linear interpoltation we change T and ONLy T and that turns into S and we use S with lerp
//cosine is cosine, but to use cosine we multiply t by pi and then subtract that value from 1, then divide by 2
//smoothstep is a different function, but perlin recommended to use 6t^5−15t^4+10t^3 instead. t^3(6t^2 - 15t + 10)

//applying this concept to 2d is simply adding more to the process
//find tx and ty, linearly interpolate tx with the min and max x of the cell
//do the same with ty
//have array R of 256 random values
//have table permutation of 256 values duplicated to make it 512 in length
//using input x we do permutationTable[x]
//we then use that value to lookup in random table i.e r[permutationTable[x]]
//for 2d noise we do r[permutation[permutation[x] + y]]