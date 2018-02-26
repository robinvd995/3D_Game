package game.client.renderer.debug;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import game.common.physics.Ray;

public class DebugRay implements IDebuggable {

	private static final Matrix4f TRANSFORMATION_MATRIX = MathHelper.createTransformationMatrix(new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f), Quaternion.identity());
	private static final int[] INDICES = {0, 1};
	
	private final float[] vertices;
	
	public DebugRay(Ray ray){
		vertices = new float[6];
		vertices[0] = ray.getOrigin().getX();
		vertices[1] = ray.getOrigin().getY();
		vertices[2] = ray.getOrigin().getZ();
		vertices[3] = ray.getDestination().getX();
		vertices[4] = ray.getDestination().getY();
		vertices[5] = ray.getDestination().getZ();
	}
	
	@Override
	public Matrix4f getTransformationMatrix() {
		return TRANSFORMATION_MATRIX;
	}

	@Override
	public float[] getVertexPositions() {
		return vertices;
	}

	@Override
	public int[] getIndices() {
		return INDICES;
	}

	@Override
	public Vector3f getLineColor() {
		return new Vector3f(1.0f, 0.5f, 0.3f);
	}

}
