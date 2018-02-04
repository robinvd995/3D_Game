package game.entity;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;

public class Transform {

	private Vector3f position;
	private Vector3f scale;
	private Vector3f rotation;
	
	public Transform(){
		position = new Vector3f();
		scale = new Vector3f(1.0F, 1.0F, 1.0F);
		rotation = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public void translate(float dx, float dy, float dz){
		position.translate(dx, dy, dz);
	}
	
	public void translateInversed(Vector3f direction, float distance){
		//inversed for camera movement
		Quaternion orientation = getOrientation().inverse();
		Vector3f delta = orientation.mult(direction);
		delta.mult(distance);
		translate(delta.getX(), delta.getY(), delta.getZ());
	}
	
	public void translate(Vector3f direction, float distance){
		Quaternion orientation = getOrientation();
		//Quaternion copy = orientation.copy().inverse();
		Vector3f delta = orientation.mult(direction);
		delta.mult(distance);
		translate(delta.getX(), delta.getY(), delta.getZ());
	}
	
	public void rotate(Vector3f angle, float amount){
		/*Vector3f rot = angle.mult(amount);
		Quaternion quat = Quaternion.fromRadianVector(rot);
		quat.normalize();
		Quaternion orientation = getOrientation().inverse();
		orientation.normalize();
		orientation = orientation.mult(quat);
		orientation.normalize();
		rotation = MathHelper.getEulerFromQuat(orientation);
		System.out.println(rotation);*/
	}
	
	public void rotate(float rx, float ry, float rz){
		rotation.add(rx, ry, rz);
	}
	
	public void setScale(float sx, float sy, float sz){
		scale.set(sx, sy, sz);
	}
	
	public Matrix4f getTransformationMatrix(){
		return MathHelper.createTransformationMatrix(position, scale, getOrientation());
	}
	
	public Vector3f getPosition(){
		return position;
	}
	
	public Vector3f getScale(){
		return scale;
	}
	
	public Vector3f getRotation(){
		return rotation;
	}
	
	public Quaternion getOrientation(){
		return Quaternion.fromRadianVector(rotation);
	}
}
