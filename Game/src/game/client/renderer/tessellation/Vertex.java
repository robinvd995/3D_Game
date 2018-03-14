package game.client.renderer.tessellation;

public class Vertex {

	private final float posX, posY, posZ;
	private final float u, v;
	private final float normX, normY, normZ;
	
	public Vertex(float posX, float posY, float posZ, float u, float v, float normX, float normY, float normZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
		this.u = u;
		this.v = v;
		this.normX = normX;
		this.normY = normY;
		this.normZ = normZ;
	}

	public float getPosX() {
		return posX;
	}

	public float getPosY() {
		return posY;
	}

	public float getPosZ() {
		return posZ;
	}

	public float getU() {
		return u;
	}

	public float getV() {
		return v;
	}

	public float getNormX() {
		return normX;
	}

	public float getNormY() {
		return normY;
	}

	public float getNormZ() {
		return normZ;
	}
}
