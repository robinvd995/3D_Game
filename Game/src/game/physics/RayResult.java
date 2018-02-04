package game.physics;

import caesar.util.Vector3f;

public class RayResult {
	
	private boolean intersect = false;
	private Vector3f point;
	
	public RayResult(boolean intersects, Vector3f point){
		this.intersect = intersects;
		this.point = point;
	}
	
	public boolean intersects(){
		return intersect;
	}
	
	public Vector3f getPoint(){
		return point;
	}
}
