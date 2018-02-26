package game.client.renderer.texture;

public class TextureData {

	private final String texture;
	
	private final int width;
	private final int height;
	
	private int minFilter = -1;
	private int magFilter = -1;
	
	private int wrapS = -1;
	private int wrapT = -1;
	
	protected TextureData(String texture, int width, int height){
		this.texture = texture;
		this.width = width;
		this.height = height;
	}
	
	public String getTexture(){
		return texture;
	}
	
	public int getMinFilter() {
		return minFilter;
	}

	public TextureData setMinFilter(int minFilter) {
		this.minFilter = minFilter;
		return this;
	}

	public int getMagFilter() {
		return magFilter;
	}

	public TextureData setMagFilter(int magFilter) {
		this.magFilter = magFilter;
		return this;
	}

	public int getWrapS() {
		return wrapS;
	}

	public TextureData setWrapS(int wrapS) {
		this.wrapS = wrapS;
		return this;
	}

	public int getWrapT() {
		return wrapT;
	}

	public TextureData setWrapT(int wrapT) {
		this.wrapT = wrapT;
		return this;
	}
	
	public boolean hasMinFilter(){
		return minFilter > -1;
	}

	public boolean hasMagFilter(){
		return magFilter > -1;
	}
	
	public boolean hasWrapS(){
		return wrapS > -1;
	}
	
	public boolean hasWrapT(){
		return wrapT > -1;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
}
