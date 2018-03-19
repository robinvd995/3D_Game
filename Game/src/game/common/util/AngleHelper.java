package game.common.util;

import caesar.util.Quaternion;
import caesar.util.Vector3f;

public class AngleHelper {
	
	public static final float toRadians(float angle){
		return (float) Math.toRadians(angle);
	}
	
	public static final float tan(float f){
		return (float) Math.tan(f);
	}
	
	public static final float sin(float f){
		return (float) Math.sin(f);
	}
	
	public static final float cos(float f){
		return (float) Math.sin(f);
	}
	
	public static Quaternion angleAxisDeg(float angle, Vector3f vec) {
		return new Quaternion((float)Math.toRadians(angle), vec);
	}
	
	public static Vector3f quaternionToEuler(Quaternion q){
		double x = Math.atan2(2 * (q.x() * q.y() + q.z() * q.w()), 1 - 2 * (q.y() * q.y() + q.z() * q.z()));
		double y = Math.asin(2 * (q.x() * q.z() - q.w() * q.y()));
		double z = Math.atan2(2 * (q.x() * q.w() + q.y() * q.z()), 1 - 2 * (q.z() * q.z() + q.w() * q.w()));
		return new Vector3f((float)x, (float)y, (float)z);
	}
	
	/*public static Vector3f quaternionToEuler(Quaternion q){
		double x = Math.atan((2 * (q.x() * q.y() + q.z() * q.w())) / (1 - 2 * (q.y() * q.y() + q.z() * q.z())));
		double y = Math.asin(2 * (q.x() * q.z() - q.w() * q.y()));
		double z = Math.atan((2 * (q.x() * q.w() + q.y() * q.z())) / (1 - 2 * (q.z() * q.z() + q.w() * q.w())));
		return new Vector3f((float)x, (float)y, (float)z);
	}*/
	
	public static Vector3f toDegrees(Vector3f vec){
		return new Vector3f((float) Math.toDegrees(vec.getX()), (float) Math.toDegrees(vec.getY()), (float) Math.toDegrees(vec.getZ()));
	}
	
	public static Vector3f quaternionToEulerDegrees(Quaternion q){
		Vector3f v = quaternionToEuler(q);
		return toDegrees(v);
	}
}
