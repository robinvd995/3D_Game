package caesar.util;

import caesar.util.interpolate.IInterpolatable;

public class Vector3f implements IInterpolatable<Vector3f>{

	private float x;
	private float y;
	private float z;

	public Vector3f(){
		this.z = 0;
	}

	public Vector3f(float f) {
		this(f, f, f);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getX(){
		return x;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public float getZ(){
		return z;
	}

	public void setZ(float z){
		this.z = z;
	}

	public static Vector3f from(String[] args){
		float x = Float.parseFloat(args[0]);
		float y = Float.parseFloat(args[1]);
		float z = Float.parseFloat(args[2]);
		return new Vector3f(x, y, z);
	}

	public String toString(){
		return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
	}

	public boolean equals(Object other){
		return other instanceof Vector3f ? equals((Vector3f) other) : false;
	}

	public boolean equals(Vector3f other){
		return super.equals(other) && z == other.z;
	}

	public Vector3f copy(){
		return new Vector3f(getX(), getY(), getZ());
	}

	public Vector3f translate(float x, float y, float z){
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3f translate(Vector3f vector){
		return translate(vector.getX(), vector.getY(), vector.getZ());
	}

	public Vector3f min(Vector3f other){
		float newX = getX() - other.getX();
		float newY = getY() - other.getY();
		float newZ = getZ() - other.getZ();
		set(newX, newY, newZ);
		return this;
	}

	public Vector3f mult(float amount){
		float newX = getX() * amount;
		float newY = getY() * amount;
		float newZ = getZ() * amount;
		set(newX, newY, newZ);
		return this;
	}
	
	public Vector3f mult(Vector3f other){
		float newX = getX() * other.getX();
		float newY = getY() * other.getY();
		float newZ = getZ() * other.getZ();
		set(newX, newY, newZ);
		return this;
	}

	public Vector3f normalize(){
		float length = length();
		float newX = getX() / length;
		float newY = getY() / length;
		float newZ = getZ() / length;
		set(newX, newY, newZ);
		return this;
	}

	public Vector3f set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public float length(){
		return (float) Math.sqrt( (getX() * getX()) + (getY() * getY()) + (getZ() * getZ()) );
	}

	public Vector3f inverse() {
		setX(getX() * -1);
		setY(getY() * -1);
		setZ(getZ() * -1);
		return this;
	}

	public Vector3f add(Vector3f other){
		setX(getX() + other.getX());
		setY(getY() + other.getY());
		setZ(getZ() + other.getZ());
		return this;
	}

	public Vector3f add(float dx, float dy, float dz){
		setX(getX() + dx);
		setY(getY() + dy);
		setZ(getZ() + dz);
		return this;
	}

	public Vector3f modules(float max){
		float newX = MathHelper.modules(getX(), max);
		float newY = MathHelper.modules(getY(), max);
		float newZ = MathHelper.modules(getZ(), max);
		set(newX, newY, newZ);
		return this;
	}

	public Vector3f sub(Vector3f other) {
		float newX = getX() - other.getX();
		float newY = getY() - other.getY();
		float newZ = getZ() - other.getZ();
		set(newX, newY, newZ);
		return this;
	}

	public Vector3f cross(Vector3f other) {
		float newX = getY() * other.getZ() - getZ() * other.getY();
		float newY = getZ() - other.getX() - getX() * other.getZ();
		float newZ = getX() - other.getY() - getY() * other.getX();
		set(newX, newY, newZ);
		return this;
	}

	public float dot(Vector3f other) {
		return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
	}

	public Vector3f scale(float scale) {
		float newX = getX() * scale;
		float newY = getY() * scale;
		float newZ = getZ() * scale;
		set(newX, newY, newZ);
		return this;
	}

	public float distanceTo(Vector3f other){
		float dx = getX() - other.getX();
		float dy = getY() - other.getY();
		float dz = getZ() - other.getZ();
		return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public static Vector3f sub(Vector3f vec0, Vector3f vec1){
		Vector3f result = vec0.copy();
		result.min(vec1);
		return result;
	}

	public static Vector3f add(Vector3f vec0, Vector3f vec1){
		Vector3f result = vec0.copy();
		result.add(vec1);
		return result;
	}

	public static Vector3f mult(Vector3f vec0, Vector3f vec1){
		Vector3f result = vec0.copy();
		result.mult(vec1);
		return result;
	}
	
	public static Vector3f mult(Vector3f vec, float amount){
		Vector3f result = vec.copy();
		result.mult(amount);
		return result;
	}
	
	public static Vector3f negate(Vector3f vec){
		float x = -vec.getX();
		float y = -vec.getY();
		float z = -vec.getZ();
		return new Vector3f(x, y, z);
	}

	@Override
	public Vector3f interpolate(double factor, Vector3f other) {
		float nx = (float) (x + ((other.x - x) * factor));
		float ny = (float) (y + ((other.y - y) * factor));
		float nz = (float) (z + ((other.z - z) * factor));
		return new Vector3f(nx, ny, nz);
	}
}
