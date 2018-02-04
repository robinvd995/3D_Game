package game.util;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class RenderUtils {

	public static IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createEmptyFloatBuffer(int size){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
		float[] data = new float[size];
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createEmptyIntBuffer(int size){
		IntBuffer buffer = BufferUtils.createIntBuffer(size);
		int[] data = new int[size];
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}