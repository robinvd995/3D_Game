package game.renderer.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;

public class SimpleModel {

	private int vao;
	private List<Integer> vbos;
	
	public SimpleModel(int vao){
		this.vao = vao;
		this.vbos = new ArrayList<Integer>();
	}
	
	public int getVao(){
		return vao;
	}
	
	public void bind(){
		GL30.glBindVertexArray(vao);
	}
	
	public void addVbo(Integer vbo){
		vbos.add(vbo);
	}
	
	public List<Integer> getVbos(){
		return vbos;
	}
} 
