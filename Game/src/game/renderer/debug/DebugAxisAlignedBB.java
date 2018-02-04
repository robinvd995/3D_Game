package game.renderer.debug;

import java.util.List;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import game.physics.AxisAlignedBB;

public class DebugAxisAlignedBB implements IDebuggable{
	
	private static final int[] INDICES = new int[]{
			0,1, 1,2, 2,3, 3,0,
			0,4, 1,5, 2,6, 3,7,
			4,5, 5,6, 6,7, 7,4
	};
	
	private final float[] positions;
	private final Matrix4f transformationMatrix;
	
	public DebugAxisAlignedBB(AxisAlignedBB aabb, Vector3f position){
		List<Vector3f> points = aabb.getAsPoints();
		positions = new float[points.size() * 3];
		int i = 0;
		for(Vector3f point : points){
			positions[i++] = point.getX();
			positions[i++] = point.getY();
			positions[i++] = point.getZ();
		}
		transformationMatrix = MathHelper.createTransformationMatrix(position, new Vector3f(1.01f, 1.01f, 1.01f), Quaternion.identity());
	}
	
	@Override
	public Matrix4f getTransformationMatrix() {
		return transformationMatrix;
	}

	@Override
	public float[] getVertexPositions() {
		return positions;
	}

	@Override
	public int[] getIndices() {
		return INDICES;
	}

	@Override
	public Vector3f getLineColor() {
		return new Vector3f(0.0f, 1.0f, 0.3f);
	}
}