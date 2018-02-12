package game.physics;

import java.util.ArrayList;
import java.util.List;

import caesar.util.GlobalAxis;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import game.util.EnumDirection;

public class Ray {

	private static final float TOLERANCE = 1e-6f;
	
	private final Vector3f origin;
	private final Vector3f destination;
	
	public Ray(Vector3f origin, Vector3f destination){
		this.origin = origin;
		this.destination = destination;
	}
	
	public Ray(Vector3f origin, Quaternion rotation, float distance){
		this.origin = origin;
		Vector3f deltaPos = rotation.mult(GlobalAxis.Z.toVector());
		deltaPos.mult(10.0f);
		destination = origin.copy().add(deltaPos);
	}
	
	public Vector3f getOrigin(){
		return origin;
	}
	
	public Vector3f getDestination(){
		return destination;
	}
	
	public RayAABBResult intersectRayWithAxisAlignedBB(AxisAlignedBB aabb, Vector3f position){
		//Compare origin to position to check which faces could be hit, disable backface collision
		//Check collision for each of the different planes the collision found
		//Compare the distance between the origin of the ray to the collision points found, the closest point is the correct point
		//Return the closest point as a ray result
		List<EnumDirection> directions = new ArrayList<EnumDirection>();
		if(origin.getX() < position.getX()){
			directions.add(EnumDirection.LEFT);
		}
		else if(origin.getX() > position.getX()){
			directions.add(EnumDirection.RIGHT);
		}
		if(origin.getY() < position.getY()){
			directions.add(EnumDirection.DOWN);
		}
		else if(origin.getY() > position.getY()){
			directions.add(EnumDirection.UP);
		}
		if(origin.getZ() < position.getZ()){
			directions.add(EnumDirection.FRONT);
		}
		else if(origin.getZ() > position.getZ()){
			directions.add(EnumDirection.BACK);
		}
		//Create the faces from the aabb
		float closestCollision = Float.MAX_VALUE;
		Vector3f closestIntersection = new Vector3f();
		boolean collided = false;
		EnumDirection side = EnumDirection.LEFT;
		for(EnumDirection dir : directions){
			RayResult result = intersectRayWithAABBFace(aabb, position, dir);
			if(result.intersects()){
				float distance = origin.distanceTo(result.getPoint());
				if(distance < closestCollision){
					closestCollision = distance;
					collided = true;
					closestIntersection = result.getPoint();
					side = dir;
				}
			}
		}
		return new RayAABBResult(collided, closestIntersection, side);
	}
	
	public RayResult intersectRayWithAABBFace(AxisAlignedBB aabb, Vector3f pos, EnumDirection dir){
		Vector3f s1 = null;
		Vector3f s2 = null;
		Vector3f s3 = null;
		switch(dir){
		case LEFT:
			s1 = new Vector3f(aabb.getMinX(pos), aabb.getMinY(pos), aabb.getMaxZ(pos));
			s2 = new Vector3f(aabb.getMinX(pos), aabb.getMaxY(pos), aabb.getMaxZ(pos));
			s3 = new Vector3f(aabb.getMinX(pos), aabb.getMinY(pos), aabb.getMinZ(pos));
			break;
		case RIGHT:
			s1 = new Vector3f(aabb.getMaxX(pos), aabb.getMinY(pos), aabb.getMaxZ(pos));
			s2 = new Vector3f(aabb.getMaxX(pos), aabb.getMaxY(pos), aabb.getMaxZ(pos));
			s3 = new Vector3f(aabb.getMaxX(pos), aabb.getMinY(pos), aabb.getMinZ(pos));
			break;
		case UP:
			s1 = new Vector3f(aabb.getMaxX(pos), aabb.getMaxY(pos), aabb.getMinZ(pos));
			s2 = new Vector3f(aabb.getMaxX(pos), aabb.getMaxY(pos), aabb.getMaxZ(pos));
			s3 = new Vector3f(aabb.getMinX(pos), aabb.getMaxY(pos), aabb.getMinZ(pos));
			break;
		case DOWN:
			s1 = new Vector3f(aabb.getMaxX(pos), aabb.getMinY(pos), aabb.getMinZ(pos));
			s2 = new Vector3f(aabb.getMaxX(pos), aabb.getMinY(pos), aabb.getMaxZ(pos));
			s3 = new Vector3f(aabb.getMinX(pos), aabb.getMinY(pos), aabb.getMinZ(pos));
			break;
		case FRONT:
			s1 = new Vector3f(aabb.getMinX(pos), aabb.getMaxY(pos), aabb.getMinZ(pos));
			s2 = new Vector3f(aabb.getMaxX(pos), aabb.getMaxY(pos), aabb.getMinZ(pos));
			s3 = new Vector3f(aabb.getMinX(pos), aabb.getMinY(pos), aabb.getMinZ(pos));
			break;
		case BACK:
			s1 = new Vector3f(aabb.getMinX(pos), aabb.getMaxY(pos), aabb.getMaxZ(pos));
			s2 = new Vector3f(aabb.getMaxX(pos), aabb.getMaxY(pos), aabb.getMaxZ(pos));
			s3 = new Vector3f(aabb.getMinX(pos), aabb.getMinY(pos), aabb.getMaxZ(pos));
			break;
		}
		return intersectRayWithSquare(s1, s2, s3);
	}
	
	public RayResult intersectRayWithSquare(Vector3f s1, Vector3f s2, Vector3f s3) {
		
		Vector3f r1 = origin.copy();
		Vector3f r2 = destination.copy();
		
		Vector3f dS21 = s2.copy().sub(s1);
		Vector3f dS31 = s3.copy().sub(s1);
		Vector3f n = dS21.copy().cross(dS31);

		Vector3f dR = r1.copy().sub(r2);

		float ndotdR = n.copy().dot(dR);

		if (Math.abs(ndotdR) < TOLERANCE) {
			return new RayResult(false, null);
		}

		float t = -n.dot(r1.copy().sub(s1)) / ndotdR;
		Vector3f M = r1.copy().add(dR.copy().scale(t));

		Vector3f dMS1 = M.copy().sub(s1);
		float u = dMS1.copy().dot(dS21);
		float v = dMS1.copy().dot(dS31);

		boolean intersect = (u >= 0.0f && u <= dS21.dot(dS21) && v >= 0.0f && v <= dS31.dot(dS31));
		return new RayResult(intersect, M);
	}
}
