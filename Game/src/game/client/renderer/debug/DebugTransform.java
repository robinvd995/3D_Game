package game.client.renderer.debug;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;

public class DebugTransform  implements IDebuggable {

	private static final float[] VERTICES = {
			0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 2.0f
	};

	private static final int[] INDICES = {
			0,1,2,3,4,5
	};

	private Vector3f position;
	private Vector3f rotation;

	public DebugTransform(Vector3f position, Vector3f rotation){
		this.position = position;
		this.rotation = rotation;
	}

	@Override
	public Matrix4f getTransformationMatrix() {
		return MathHelper.createTransformationMatrix(position, new Vector3f(1.0f, 1.0f, 1.0f), Quaternion.fromVector(rotation));
	}

	@Override
	public float[] getVertexPositions() {
		return VERTICES;
	}

	@Override
	public int[] getIndices() {
		return INDICES;
	}

	@Override
	public Vector3f getLineColor() {
		return new Vector3f(0.0f, 0.5f, 1.0f);
	}
}