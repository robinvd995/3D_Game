package game.client.renderer.world;

import game.client.renderer.texture.TextureData;

public class CelestialBody {

	private final TextureData textureData;
	private final float maxProgress;
	
	private float progress;
	
	public CelestialBody(float maxProgress, TextureData texture){
		this.maxProgress = maxProgress;
		this.textureData = texture;
	}
	
	public void setProgress(float progress){
		this.progress = progress;
	}
	
	public void addProgress(float delta){
		this.progress = (progress + delta) % maxProgress;
	}
	
	public float getProgress(){
		return progress;
	}
	
	public float maxProgress(){
		return maxProgress;
	}
	
	public TextureData getTextureData(){
		return textureData;
	}
}
