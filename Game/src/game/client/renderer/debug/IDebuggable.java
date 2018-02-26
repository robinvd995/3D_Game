package game.client.renderer.debug;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;

public interface IDebuggable {
	
	Matrix4f getTransformationMatrix();
	float[] getVertexPositions();
	int[] getIndices();
	Vector3f getLineColor();
}
