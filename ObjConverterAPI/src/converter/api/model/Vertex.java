package converter.api.model;

import caesar.util.Vector2f;
import caesar.util.Vector3f;

public class Vertex {

	public Vector3f position;
	public Vector2f uv;
	public Vector3f normal;
	
	public Vertex(Vector3f position, Vector2f uv, Vector3f normal) {
		this.position = position;
		this.uv = uv;
		this.normal = normal;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector2f getUv() {
		return uv;
	}

	public void setUv(Vector2f uv) {
		this.uv = uv;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	public String toString(){
		return "Vertex(position = " + position + ", uv = " + uv + ", normal = " + normal + ")";
	}
	
	public boolean equals(Object other){
		return other instanceof Vertex ? equals((Vertex)other) : false;
	}
	
	public boolean equals(Vertex other){
		return position.equals(other.position) && uv.equals(other.uv) && normal.equals(other.normal);
	}
}
