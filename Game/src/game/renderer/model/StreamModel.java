package game.renderer.model;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import game.util.RenderUtils;

public class StreamModel extends ModelBase{
	
	private static final String INDICES_ID = "indices";
	
	private HashMap<String,Integer> vboMap = new HashMap<String,Integer>();
	private HashMap<String,Integer> attribMap = new HashMap<String,Integer>();
	
	private boolean hasIndexBuffer = false;
	
	private int size;
	private int drawType;
	
	public StreamModel(int vao, int drawType){
		super(vao);
		this.drawType = drawType;
	}
	
	public void createIndexBuffer(int size){
		this.hasIndexBuffer = true;
		this.size = size;
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, RenderUtils.createEmptyIntBuffer(size), drawType);
		vboMap.put(INDICES_ID, vbo);
	}
	
	public void createIndexBuffer(int[] data){
		this.hasIndexBuffer = true;
		this.size = data.length;
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, RenderUtils.storeDataInIntBuffer(data), drawType);
		vboMap.put(INDICES_ID, vbo);
	}
	
	public void createDataBuffer(String id, int attributeNumber, int coordSize, int size){
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RenderUtils.createEmptyIntBuffer(size), drawType);
		GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vboMap.put(id, vbo);
		attribMap.put(id, attributeNumber);
	}
	
	public void createDataBuffer(String id, int attributeNumber, int coordSize, float[] data){
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RenderUtils.storeDataInFloatBuffer(data), drawType);
		GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		vboMap.put(id, vbo);
		attribMap.put(id, attributeNumber);
	}
	
	public void updateIndexBuffer(int[] data){
		this.size = data.length;
		int vbo = getVbo(INDICES_ID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data.length * 4, drawType);
		GL15.glBufferSubData(GL15.GL_ELEMENT_ARRAY_BUFFER, 0, RenderUtils.storeDataInIntBuffer(data));
	}
	
	public void updateDataBuffer(String id, int coordSize, float[] data){
		int vbo = getVbo(id);
		int attrib = getAttribute(id);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data.length * 4, drawType);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, RenderUtils.storeDataInFloatBuffer(data));
		GL20.glVertexAttribPointer(attrib, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void setSize(int size){
		if(hasIndexBuffer)
			throw new IllegalArgumentException("A index buffer is bound to this model, a size can not need be set manually!");
		this.size = size;
	}
	
	private int getVbo(String id){
		return vboMap.get(id);
	}

	private int getAttribute(String id){
		return attribMap.get(id);
	}
	
	public int getSize(){
		return size;
	}
	
	public void delete(){
		super.delete();
		for(int vbo : vboMap.values()){
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	/*public void render() {
		GL11.glDrawElements(GL11.GL_LINES, size, GL11.GL_UNSIGNED_INT, 0);
	}*/
}
