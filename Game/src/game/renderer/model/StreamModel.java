package game.renderer.model;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import game.util.RenderUtils;

public class StreamModel extends ModelBase{
	
	private static final String INDICES_ID = "indices";
	
	private HashMap<String,Integer> vboMap = new HashMap<String,Integer>();
	
	public StreamModel(int vao){
		super(vao);
	}
	
	public void createIndexBuffer(int size){
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, RenderUtils.createEmptyIntBuffer(size), GL15.GL_STREAM_DRAW);
		vboMap.put(INDICES_ID, vbo);
	}
	
	public void createDataBuffer(String id, int size){
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RenderUtils.createEmptyIntBuffer(size), GL15.GL_STREAM_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vboMap.put(id, vbo);
	}
	
	public void updateIndexBuffer(int[] data){
		int vbo = getVbo(INDICES_ID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, RenderUtils.storeDataInIntBuffer(data));
	}
	
	public void updateDataBuffer(String id, float[] data){
		int vbo = getVbo(id);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, RenderUtils.storeDataInFloatBuffer(data));
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private int getVbo(String id){
		return vboMap.get(id);
	}

	public void render(int size) {
		GL11.glDrawElements(GL11.GL_LINES, size, GL11.GL_UNSIGNED_INT, 0);
	}
}
