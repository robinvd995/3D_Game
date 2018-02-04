package converter.render;

import caesar.util.GlobalAxis;
import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;

public class Camera {

	public static final float ROTATION_SENSITIVITY = 1.0F;
	public static final float ZOOM_SENSITIVITY = 0.05F;
	public static final float ZOOM_SCALAR = 0.0F;

	private Vector3f rotationPoint = new Vector3f(0,0,0);
	private Vector3f position = new Vector3f();

	private Vector3f rotation = new Vector3f();

	private float distance = 10.0f;

	public Camera(){
		position = new Vector3f(0.0F, 5.0F, 10.0f);
	}

	public void moveCamera(float rotationX, float rotationY, float zoom){

		float maxAngle = (float) (Math.PI * 2);
		
		float rotX = modules(rotation.getX() + (float) Math.toRadians(-rotationY), maxAngle);
		float rotY = modules(rotation.getY() + (float) Math.toRadians(-rotationX), maxAngle);
		
		rotation.set(rotX, rotY, 0.0f);

		distance = MathHelper.clampf(distance + zoom * ZOOM_SENSITIVITY, 1.0F, 1000.0F);

		Quaternion quat = Quaternion.fromRadianVector(rotation);
		Quaternion inversed = quat.copy().inverse();
		position = inversed.mult(GlobalAxis.Z.toVector(distance));
	}

	private float modules(float f, float max){
		if(f > max){
			return f - max;
		}
		else if(f < 0.0f){
			return f + max;
		}
		return f;
	}

	public Matrix4f createViewMatrix(){
		return MathHelper.createViewMatrix(position, Quaternion.fromRadianVector(rotation));
	}
}
