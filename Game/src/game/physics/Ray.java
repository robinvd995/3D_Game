package game.physics;

import caesar.util.Vector3f;

public class Ray {

	private static final float TOLERANCE = 1e-6f;
	
	private final Vector3f origin;
	private final Vector3f destination;
	
	public Ray(Vector3f origin, Vector3f destination){
		this.origin = origin;
		this.destination = destination;
	}
	
	public Vector3f getOrigin(){
		return origin;
	}
	
	public Vector3f getDestination(){
		return destination;
	}
	
	public RayResult intersectRayWithAxisAlignedBB(AxisAlignedBB aabb, Vector3f position){
		//Compare origin to position to check which faces could be hit, disable backface collision
		//Create the faces from the aabb
		//Check collision for each of the different planes the collision found
		//Compare the distance between the origin of the ray to the collision points found, the closest point is the correct point
		//Return the closest point as a ray result
		return null;
	}
	
	public RayResult intersectRayWithSquare(Vector3f S1, Vector3f S2, Vector3f S3) {
		
		Vector3f R1 = origin.copy();
		Vector3f R2 = destination.copy();
		
		Vector3f dS21 = S2.copy().sub(S1);
		Vector3f dS31 = S3.copy().sub(S1);
		Vector3f n = dS21.copy().cross(dS31);

		Vector3f dR = R1.copy().sub(R2);

		float ndotdR = n.copy().dot(dR);

		if (Math.abs(ndotdR) < TOLERANCE) {
			return new RayResult(false, null);
		}

		float t = -n.dot(R1.copy().sub(S1)) / ndotdR;
		Vector3f M = R1.copy().add(dR.copy().scale(t));

		Vector3f dMS1 = M.copy().sub(S1);
		float u = dMS1.copy().dot(dS21);
		float v = dMS1.copy().dot(dS31);

		boolean intersect = (u >= 0.0f && u <= dS21.dot(dS21) && v >= 0.0f && v <= dS31.dot(dS31));
		return new RayResult(intersect, M);
	}
}
