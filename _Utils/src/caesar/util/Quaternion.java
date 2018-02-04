package caesar.util;


public class Quaternion {
	private float x, y, z, w;

	private Quaternion() {
		reset();
	}

	public Quaternion(float x, float y, float z, float w) {
		set(x, y, z, w);
	}

	public Quaternion(float angle, Vector3f vec) {
		float s = (float)Math.sin(angle / 2);

		x = vec.getX() * s;
		y = vec.getY() * s;
		z = vec.getZ() * s;
		w = (float)Math.cos(angle / 2);
	}
	
	public Quaternion(Vector3f vector) {
		double c1 = Math.cos(vector.getY()/2);
		double s1 = Math.sin(vector.getY()/2);
		double c2 = Math.cos(vector.getZ()/2);
		double s2 = Math.sin(vector.getZ()/2);
		double c3 = Math.cos(vector.getX()/2);
		double s3 = Math.sin(vector.getX()/2);
		double c1c2 = c1*c2;
		double s1s2 = s1*s2;
		w =(float) (c1c2*c3 - s1s2*s3);
		x =(float) (c1c2*s3 + s1s2*c3);
		y =(float) (s1*c2*c3 + c1*s2*s3);
		z =(float) (c1*s2*c3 - s1*c2*s3);
	}

	public Quaternion(Quaternion q) {
		set(q);
	}

	public Quaternion copy() {
		return new Quaternion(this);
	}

	public float x() {
		return x;
	}

	public Quaternion x(float x) {
		this.x = x;
		return this;
	}

	public float y() {
		return y;
	}

	public Quaternion y(float y) {
		this.y = y;
		return this;
	}

	public float z() {
		return z;
	}

	public Quaternion z(float z) {
		this.z = z;
		return this;
	}

	public float w() {
		return w;
	}

	public Quaternion w(float w) {
		this.w = w;
		return this;
	}

	public Quaternion set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}

	public Quaternion set(Quaternion q) {
		return set(q.getX(), q.getY(), q.getZ(), q.getW());
	}

	public Quaternion reset() {
		x = y = z = 0;
		w = 1;
		return this;
	}

	public float length() {
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}

	public Quaternion normalize() {
		float length = length();
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		return this;
	}

	public float dot(Quaternion q) {
		return x * q.getX() + y * q.getY() + z * q.getZ() + w * q.getW();
	}

	public Quaternion add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;

		return this;
	}

	public Quaternion add(Quaternion q) {
		return add(q.getX(), q.getY(), q.getZ(), q.getW());
	}

	public Quaternion sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;

		return this;
	}

	public Quaternion sub(Quaternion q) {
		return sub(q.getX(), q.getY(), q.getZ(), q.getW());
	}

	public Quaternion mult(float f) {
		this.x *= f;
		this.y *= f;
		this.z *= f;
		this.w *= f;

		return this;
	}

	public Vector3f mult(Vector3f v) {
		Vector3f quatVector = new Vector3f(x, y, z);

		Vector3f uv = new Vector3f(quatVector.getY() * v.getZ() - v.getY() * quatVector.getZ(), quatVector.getZ() * v.getX() - v.getZ() * quatVector.getX(), quatVector.getX() * v.getY() - v.getX() * quatVector.getY());
		Vector3f uuv = new Vector3f(quatVector.getY() * uv.getZ() - uv.getY() * quatVector.getZ(), quatVector.getZ() * uv.getX() - uv.getZ() * quatVector.getX(), quatVector.getX() * uv.getY() - uv.getX() * quatVector.getY());

		uv.setX(uv.getX() * w * 2);
		uv.setY(uv.getY() * w * 2);
		uv.setZ(uv.getZ() * w * 2);

		uuv.setX(uuv.getX() * 2);
		uuv.setY(uuv.getY() * 2);
		uuv.setZ(uuv.getZ() * 2);

		return new Vector3f(v.getX() + uv.getX() + uuv.getX(), v.getY() + uv.getY() + uuv.getY(), v.getZ() + uv.getZ() + uuv.getZ());
	}

	public Quaternion mult(Quaternion q) {
		float xx = w * q.getX() + x * q.getW() + y * q.getZ() - z * q.getY();
		float yy = w * q.getY() + y * q.getW() + z * q.getX() - x * q.getZ();
		float zz = w * q.getZ() + z * q.getW() + x * q.getY() - y * q.getX();
		float ww = w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ();

		x = xx;
		y = yy;
		z = zz;
		w = ww;

		return this;
	}

	public Quaternion conjugate() {
		x *= -1;
		y *= -1;
		z *= -1;
		return this;
	}

	public Quaternion inverse() {
		return normalize().conjugate();
	}

	public Matrix4f toMatrix() {
		return toMatrix(null);
	}

	public Matrix4f toMatrix(Matrix4f mat4) {
		if(mat4 == null)
			mat4 = new Matrix4f();

		/*mat4.set(new float[] {
				1 - 2 * y * y - 2 * z * z, 2 * x * y + 2 * w * z, 2 * x * z - 2 * w * y, 0,
				2 * x * y - 2 * w * z, 1 - 2 * x * x - 2 * z * z, 2 * y * z + 2 * w * x, 0,
				2 * x * z + 2 * w * y, 2 * y * z - 2 * w * x, 1 - 2 * x * x - 2 * y * y, 0,
				0, 0, 0, 1,
		});*/
		mat4.m00 = 1 - 2 * y * y - 2 * z * z;
		mat4.m01 = 2 * x * y + 2 * w * z;
		mat4.m02 = 2 * x * z - 2 * w * y;
		mat4.m03 = 0;

		mat4.m10 = 2 * x * y - 2 * w * z;
		mat4.m11 = 1 - 2 * x * x - 2 * z * z;
		mat4.m12 = 2 * y * z + 2 * w * x;
		mat4.m13 = 0;

		mat4.m20 = 2 * x * z + 2 * w * y;
		mat4.m21 = 2 * y * z - 2 * w * x;
		mat4.m22 = 1 - 2 * x * x - 2 * y * y;
		mat4.m23 = 0;

		mat4.m30 = 0;
		mat4.m31 = 0;
		mat4.m32 = 0;
		mat4.m33 = 1;

		return mat4;
	}
	
	public static Quaternion fromVector(Vector3f vec){
		Quaternion q = new Quaternion();
		q.mult(new Quaternion((float)Math.toRadians(vec.getX()), GlobalAxis.X.toVector()));
		q.mult(new Quaternion((float)Math.toRadians(vec.getY()), GlobalAxis.Y.toVector()));
		q.mult(new Quaternion((float)Math.toRadians(vec.getZ()), GlobalAxis.Z.toVector()));
		return q;
	}

	public static Quaternion fromRadianVector(Vector3f vec){
		Quaternion q = new Quaternion();
		q.mult(new Quaternion((float)(vec.getX()), GlobalAxis.X.toVector()));
		q.mult(new Quaternion((float)(vec.getY()), GlobalAxis.Y.toVector()));
		q.mult(new Quaternion((float)(vec.getZ()), GlobalAxis.Z.toVector()));
		return q;
	}

	public static Quaternion slerp(float amount, Quaternion value1, Quaternion value2){
		if ((value1 == null) || (value2 == null)){
			return null;
		}

		if (amount < 0.0)
			return value1;
		else if (amount > 1.0)
			return value2;

		float dot = value1.dot(value2);
		float x2, y2, z2, w2;
		if (dot < 0.0){
			dot = 0.0f - dot;
			x2 = 0.0f - value2.getX();
			y2 = 0.0f - value2.getY();
			z2 = 0.0f - value2.getZ();
			w2 = 0.0f - value2.getW();
		}
		else{
			x2 = value2.getX();
			y2 = value2.getY();
			z2 = value2.getZ();
			w2 = value2.getW();
		}

		float t1, t2;

		final float EPSILON = 0.0001f;
		if ((1.0 - dot) > EPSILON){
			float angle = (float) Math.acos(dot);
			float sinAngle = (float) Math.sin(angle);
			t1 = (float) (Math.sin((1.0 - amount) * angle) / sinAngle);
			t2 = (float) (Math.sin(amount * angle) / sinAngle);
		}
		else{
			t1 = 1.0f - amount;
			t2 = amount;
		}

		return new Quaternion(
				(value1.getX() * t1) + (x2 * t2),
				(value1.getY() * t1) + (y2 * t2),
				(value1.getZ() * t1) + (z2 * t2),
				(value1.getW() * t1) + (w2 * t2));
	}

	public Vector3f toEuler(){
		double attitude;
		double heading;
		double bank;
		double test = x*y + z*w;
		/*if (test > 0.499) { // singularity at north pole
			heading = 2 * Math.atan2(x,w);
			attitude = Math.PI/2;
			bank = 0;
			return new Vector3f((float) attitude, (float) heading, (float) bank);
		}
		if (test < -0.499) { // singularity at south pole
			heading = -2 * Math.atan2(x,w);
			attitude = - Math.PI/2;
			bank = 0;
			return new Vector3f((float) attitude, (float) heading, (float) bank);
		}*/
	    double sqx = x*x;
	    double sqy = y*y;
	    double sqz = z*z;
	    heading = Math.atan2(2*y*w-2*x*z , 1 - 2*sqy - 2*sqz);
		attitude = Math.asin(2*test);
		bank = Math.atan2(2*x*w-2*y*z , 1 - 2*sqx - 2*sqz);
		return new Vector3f((float) bank, (float) heading, (float) attitude);
	}
	
	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public float getZ(){
		return z;
	}

	public float getW(){
		return w;
	}
	
	public static Quaternion identity(){
		return new Quaternion();
	}
}
