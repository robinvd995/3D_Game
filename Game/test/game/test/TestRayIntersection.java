package game.test;

import org.junit.Test;

import caesar.util.Vector3f;

public class TestRayIntersection {

	@Test
	public void testRayIntersection(){
		Vector3f R1 = new Vector3f(1.0f, 0.0f, -1.0f);
        Vector3f R2 = new Vector3f(-2.0f, 0.0f,  1.0f);

        Vector3f S1 = new Vector3f(-1.0f, 1.0f, 0.0f);
        Vector3f S2 = new Vector3f( 1.0f, 1.0f, 0.0f);
        Vector3f S3 = new Vector3f(-1.0f,-1.0f, 0.0f);

        RayResult result = intersectRayWithSquare(R1, R2, S1, S2, S3);
        System.out.println(result.getPoint());
        assert result.intersects();

        R1 = new Vector3f(1.5f, 1.5f, -1.0f);
        R2 = new Vector3f(1.5f, 1.5f,  1.0f);

        result = intersectRayWithSquare(R1, R2, S1, S2, S3);
        assert !result.intersects();
	}

	public static RayResult intersectRayWithSquare(Vector3f R1, Vector3f R2, Vector3f S1, Vector3f S2, Vector3f S3) {
		// 1.
		Vector3f dS21 = S2.copy().sub(S1);
		Vector3f dS31 = S3.copy().sub(S1);
		Vector3f n = dS21.copy().cross(dS31);

		// 2.
		Vector3f dR = R1.copy().sub(R2);

		float ndotdR = n.copy().dot(dR);

		if (Math.abs(ndotdR) < 1e-6f) { // Choose your tolerance
			return new RayResult(false, null);
		}

		float t = -n.dot(R1.copy().sub(S1)) / ndotdR;
		Vector3f M = R1.copy().add(dR.copy().scale(t));

		// 3.
		Vector3f dMS1 = M.copy().sub(S1);
		float u = dMS1.copy().dot(dS21);
		float v = dMS1.copy().dot(dS31);

		// 4.
		boolean intersect = (u >= 0.0f && u <= dS21.dot(dS21)
				&& v >= 0.0f && v <= dS31.dot(dS31));
		return new RayResult(intersect, M);
	}
	
	private static class RayResult {
		
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
}
