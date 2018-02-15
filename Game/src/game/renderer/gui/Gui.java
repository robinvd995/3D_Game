package game.renderer.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import game.renderer.gui.component.IGuiComponent;
import game.renderer.model.ModelLoader;
import game.renderer.shader.GuiShader;
import game.renderer.texture.TextureManager;

public abstract class Gui {

	private List<IGuiComponent> components;
	
	protected int guiWidth;
	protected int guiHeight;
	
	private String currentBoundTexture = "";
	
	public Gui(){
		this.components = new ArrayList<IGuiComponent>();
	}
	
	public void init(int width, int height){
		this.guiWidth = width;
		this.guiHeight = height;
		initGui();
	}
	
	protected abstract void initGui();
	
	protected void addComponent(IGuiComponent component){
		component.onComponentAdded(this);
		components.add(component);
	}
	
	public void renderGui(GuiShader shader){
		currentBoundTexture = "";
		for(IGuiComponent comp : components){
			shader.loadElementPosition(comp.getPosition());
			comp.renderComponent(this);
		}
	}
	
	public GuiQuad createQuad(int x, int y, int width, int height, int u, int v, int textureWidth, int textureHeight){
		float x0 = (float)x / (float)guiWidth;
		float y0 = (float)y / (float)guiHeight;
		float x1 = (float)(x + width) / (float)guiWidth;
		float y1 = (float)(y + height) / (float)guiHeight;
		float u0 = (float)u / (float)textureWidth;
		float v0 = (float)v / (float)textureHeight;
		float u1 = (float)(u + width) / (float)textureWidth;
		float v1 = (float)(v + height) / (float)textureHeight;
		
		float[] vertices = {
				x0, y0,
				x1, y0,
				x1, y1,
				x0, y1,
		};
		
		float[] uvs = {
				u0, v0,
				u1, v0,
				u1, v1,
				u0, v1
		};
		
		int[] indices = {
				0,1,2,
				2,3,0
		};
		
		return ModelLoader.INSTANCE.loadGuiComponentQuad(vertices, uvs, indices);
	}
	
	public void dispose(){
		for(IGuiComponent gui : components){
			gui.onComponentDeleted(this);
		}
		components.clear();
	}
	
	public void bindTexture(String texture){
		if(currentBoundTexture.equals(texture)){
			return;
		}
		TextureManager.bindTexture(texture);
		currentBoundTexture = texture;
	}
	
	public static class GuiQuad {
		
		private int vao;
		
		private List<Integer> vbos;
		
		public GuiQuad(int vao){
			this.vao = vao;
			vbos = new ArrayList<Integer>();
		}
		
		public void bindQuad(){
			GL30.glBindVertexArray(vao);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
		}
		
		public void unbindQuad(){
			GL30.glBindVertexArray(0);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
		}
		
		public void deleteQuad(){
			GL30.glDeleteVertexArrays(vao);
			for(int vbo : vbos){
				GL15.glDeleteBuffers(vbo);
			}
		}
		
		public void addVbo(int vbo){
			vbos.add(vbo);
		}
		
		public void renderQuad(){
			GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
		}
	}
}
