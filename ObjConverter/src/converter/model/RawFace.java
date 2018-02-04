package converter.model;

import converter.api.model.Vertex;

public class RawFace {

	private final Vertex[] vertices;
	
	private int counter;
	
	public RawFace(int faceSize){
		vertices = new Vertex[faceSize];
	}
	
	public Vertex[] getVertices(){
		return vertices;
	}
	
	public void addVertex(Vertex vertex){
		if(counter < vertices.length){
			vertices[counter] = vertex;
			counter++;
		}
	}
	
	public int size(){
		return vertices.length;
	}
}
