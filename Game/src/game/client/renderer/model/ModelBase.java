package game.client.renderer.model;

import org.lwjgl.opengl.GL30;

public abstract class ModelBase {

	private final int vao;
	
	protected ModelBase(int vao){
		this.vao = vao;
	}
	
	public void bindModel(){
		GL30.glBindVertexArray(vao);
	}
	
	public void unbindModel(){
		GL30.glBindVertexArray(0);
	}
	
	public final int getVao(){
		return vao;
	}
	
	public void delete(){
		GL30.glDeleteVertexArrays(vao);
	}
}
