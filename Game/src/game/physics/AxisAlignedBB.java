package game.physics;

import java.util.ArrayList;
import java.util.List;

import caesar.util.Vector3f;
import game.util.EnumDirection;

public class AxisAlignedBB {

	private float minX, minY, minZ;
	private float maxX, maxY, maxZ;
	
	public AxisAlignedBB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public boolean intersect(Vector3f aabbPos, Vector3f point){
		return intersect(aabbPos.getX(), aabbPos.getY(), aabbPos.getZ(), point.getX(), point.getY(), point.getZ());
	}
	
	private boolean intersect(float aabbx, float aabby, float aabbz, float x, float y, float z){
		return x >= minX + aabbx && x < maxX + aabbx && y >= minY + aabby && y < maxY + aabby && z >= minZ + aabbz && z < maxZ + aabbz;
	}
	
	public boolean intersect(AxisAlignedBB other, Vector3f myPos, Vector3f otherPos){
		return intersect(other, myPos.getX(), myPos.getY(), myPos.getZ(), otherPos.getX(), otherPos.getY(), otherPos.getZ());
	}
	
	private boolean intersect(AxisAlignedBB other, float myX, float myY, float myZ, float otherX, float otherY, float otherZ){
		return  minX + myX <= other.maxX + otherX && maxX + myX >= other.minX + otherX && 
				minY + myY <= other.maxY + otherY && maxY + myY >= other.minY + otherY &&
				minZ + myZ <= other.maxZ + otherZ && maxZ + myZ >= other.minZ + otherZ;
	}
	
	public boolean intersectSide(EnumDirection side, AxisAlignedBB other, float myX, float myY, float myZ, float otherX, float otherY, float otherZ){
		return false;
	}
	
	public float getValueForSide(EnumDirection side){
		switch(side){
		case UP: return maxY;
		case DOWN: return minY;
		case LEFT: return minX;
		case RIGHT: return maxX;
		case FRONT: return minZ;
		case BACK: return maxZ;
		default: return 0.0f;
		}
	}
	
	public List<Vector3f> getAsPoints(){
		List<Vector3f> points = new ArrayList<Vector3f>();
		points.add(new Vector3f(minX, minY, minZ));
		points.add(new Vector3f(maxX, minY, minZ));
		points.add(new Vector3f(maxX, minY, maxZ));
		points.add(new Vector3f(minX, minY, maxZ));
		points.add(new Vector3f(minX, maxY, minZ));
		points.add(new Vector3f(maxX, maxY, minZ));
		points.add(new Vector3f(maxX, maxY, maxZ));
		points.add(new Vector3f(minX, maxY, maxZ));
		return points;
	}
	
	public float getMinX() {
		return minX;
	}

	public float getMinX(Vector3f pos) {
		return minX + pos.getX();
	}
	
	public float getMinY() {
		return minY;
	}
	
	public float getMinY(Vector3f pos){
		return minY + pos.getY();
	}

	public float getMinZ() {
		return minZ;
	}
	
	public float getMinZ(Vector3f pos){
		return minZ + pos.getZ();
	}

	public float getMaxX() {
		return maxX;
	}

	public float getMaxX(Vector3f pos){
		return maxX + pos.getX();
	}
	
	public float getMaxY() {
		return maxY;
	}

	public float getMaxY(Vector3f pos){
		return maxY + pos.getY();
	}
	
	public float getMaxZ() {
		return maxZ;
	}
	
	public float getMaxZ(Vector3f pos){
		return maxZ + pos.getZ();
	}

	public float getWidth(){
		return maxX - minX;
	}
	
	public float getHeight(){
		return maxY - minY;
	}
	
	public float getDepth(){
		return maxZ - minZ;
	}
}