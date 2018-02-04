package caesar.util;

public class MathHelper {

	public static Matrix4f createTransformationMatrix(Vector3f position, float sx, float sy, float sz, Quaternion quat){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.mul(matrix, quat.toMatrix(), matrix);
		Matrix4f.scale(new Vector3f(sx, sy, sz), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f rot, float sx, float sy, float sz){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(position, matrix, matrix);
		Matrix4f.rotate(rot.getX(), GlobalAxis.X.toVector(), matrix, matrix);
		Matrix4f.rotate(rot.getY(), GlobalAxis.Y.toVector(), matrix, matrix);
		Matrix4f.rotate(rot.getZ(), GlobalAxis.Z.toVector(), matrix, matrix);
		Matrix4f.scale(new Vector3f(sx, sy, sz), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.getX(), scale.getY(), 1.0f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Vector3f position, Quaternion rotation){
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		Matrix4f.mul(m, rotation.toMatrix(), m);
		Vector3f cameraPos = position.copy();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.getX(), -cameraPos.getY(), -cameraPos.getZ());
		Matrix4f.translate(negativeCameraPos, m, m);
		return m;
	}
	
	public static Matrix4f createProjectionMatrix(int displayWidth, int displayHeight, float fov, float nearPlane, float farPlane){
		float aspectRatio = (float) displayWidth / (float) displayHeight;
		float yScale = ((1F / AngleHelper.tan(AngleHelper.toRadians(fov / 2F))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustrumLength = farPlane - nearPlane;
		
		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustrumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustrumLength);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}
	
	public static float clampf(float value, float min, float max){
		if(value < min){
			return min;
		}
		if(value > max){
			return max;
		}
		return value;
	}
	
	public static double clamp(double value){
		if(value < 0.0){
			return 0.0;
		}
		if(value > 1.0){
			return 1.0;
		}
		return value;
	}
	
	public static float lerp(float start, float end, float percentage){
		return (start + percentage * (end - start));
	}
	
	public static Vector3f multVector3f(Vector3f v1, float f){
		return new Vector3f(v1.getX() * f, v1.getY() * f, v1.getZ() * f);
	}
	
	public static Vector3f add(Vector3f v1, Vector3f v2){
		return new Vector3f(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
	}
	
	public static float dist(Vector3f v1, Vector3f v2){
		float dx = v2.getX() - v1.getX();
		float dy = v2.getY() - v1.getY();
		float dz = v2.getZ() - v1.getZ();
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static Vector3f worldToScreenSpace(Vector3f position, Matrix4f viewMatrix, Matrix4f proMatrix){
		Vector4f coords = new Vector4f(position.getX(), position.getY(), position.getZ(), 1.0f);
		Matrix4f.transform(viewMatrix, coords, coords);
		Matrix4f.transform(proMatrix, coords, coords);
		Vector3f screenCoords = clipSpaceToScreenSpace(coords);
		return screenCoords;
	}
	
	private static Vector3f clipSpaceToScreenSpace(Vector4f coords){
		if(coords.getW() < 0){
			return null;
		}
		Vector3f screenCoords = new Vector3f(((coords.getX()) / coords.getW()) / 2f, 1 - (((coords.getY() / coords.getW()) + 1) / 2f), coords.getZ());
		return screenCoords;
	}

	public static Matrix4f createTransformationMatrix(Vector3f position, Vector3f scale, Quaternion orientation) {
		return createTransformationMatrix(position, scale.getX(), scale.getY(), scale.getZ(), orientation);
	}
	
	public static float modules(float f, float max){
		if(f > max){
			return f - max;
		}
		else if(f < 0.0f){
			return f + max;
		}
		return f;
	}
	
	public static Vector3f getEulerFromQuat(Quaternion q){
		double ysqr = q.y() * q.y();
		double t0 = -2.0f * (ysqr + q.z() * q.z()) + 1.0f;
		double t1 = +2.0f * (q.x() * q.y() - q.w() * q.z());
		double t2 = -2.0f * (q.x() * q.z() + q.w() * q.y());
		double t3 = +2.0f * (q.y() * q.z() - q.w() * q.x());
		double t4 = -2.0f * (q.x() * q.x() + ysqr) + 1.0f;

		t2 = t2 > 1.0f ? 1.0f : t2;
		t2 = t2 < -1.0f ? -1.0f : t2;

		double pitch = Math.asin(t2);
		double roll = Math.atan2(t3, t4);
		double yaw = Math.atan2(t1, t0);
		
		return new Vector3f((float)roll, (float)pitch, (float)yaw);
	}
}
