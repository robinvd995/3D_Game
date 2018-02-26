package game.client.renderer.texture;

import caesar.util.Vector4f;

public class MappedTexture {

	private final float textureCoordX;
	private final float textureCoordY;
	private final float width;
	private final float height;
	
	public MappedTexture(float texPosX, float texPosY, float width, float height){
		this.textureCoordX = texPosX;
		this.textureCoordY = texPosY;
		this.width = width;
		this.height = height;
	}
	
	public float getU(){
		return textureCoordX;
	}
	
	public float getV(){
		return textureCoordY;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
	public Vector4f toVector(){
		return new Vector4f(getU(), getV(), getWidth(), getHeight());
	}
}
