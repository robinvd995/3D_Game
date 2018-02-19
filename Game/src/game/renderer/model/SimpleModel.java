package game.renderer.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;

public class SimpleModel extends ModelBase{

	private final int size;
	private List<Integer> vbos;
	
	public SimpleModel(int vao, int size){
		super(vao);
		this.vbos = new ArrayList<Integer>();
		this.size = size;
	}
	
	public void addVbo(Integer vbo){
		vbos.add(vbo);
	}
	
	public List<Integer> getVbos(){
		return vbos;
	}
	
	public int getSize(){
		return size;
	}
	
	public void delete(){
		super.delete();
		for(int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}
} 
