package game.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class JUtils {

	public static FloatBuffer storeDataInFloatBuffer(float[] data, boolean flip){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		if(flip)
			buffer.flip();
		return buffer;
	}
	
	public static IntBuffer storeDataInIntBuffer(int[] data, boolean flip){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		if(flip)
			buffer.flip();
		return buffer;
	}
}