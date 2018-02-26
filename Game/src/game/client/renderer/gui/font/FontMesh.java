package game.client.renderer.gui.font;

public class FontMesh {

	private final int[] indices;
	private final float[] vertices;
	private final float[] uvs;
	
	public FontMesh(int[] indices, float[] vertices, float[] uvs){
		this.indices = indices;
		this.vertices = vertices;
		this.uvs = uvs;
	}
	
	public int[] getIndices(){
		return indices;
	}
	
	public float[] getVertices(){
		return vertices;
	}
	
	public float[] getUvs(){
		return uvs;
	}
	
	public int getVertexCount(){
		return vertices.length;
	}
	
	public int getUvCount(){
		return uvs.length;
	}
	
	public int getSize(){
		return indices.length;
	}
}
