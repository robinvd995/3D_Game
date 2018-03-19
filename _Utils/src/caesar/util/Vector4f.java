package caesar.util;

public class Vector4f {

	private float x;
	private float y;
	private float z;
	private float w;
	
	public Vector4f(){
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Vector4f(float f) {
		this(f, f, f, f);
	}
	
	public Vector4f(Vector2f vec, float z, float w){
		this(vec.getX(), vec.getY(), z, w);
	}
	
	public Vector4f(Vector3f vec, float w){
		this(vec.getX(), vec.getY(), vec.getZ(), w);
	}
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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
	
	public float getW(){
		return w;
	}
	
	public void setW(float w){
		this.w = w;
	}
	
	public void set(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public static Vector4f from(String[] args){
		float x = Float.parseFloat(args[0]);
		float y = Float.parseFloat(args[1]);
		float z = Float.parseFloat(args[2]);
		float w = Float.parseFloat(args[3]);
		return new Vector4f(x, y, z, w);
	}
	
	@Override
	public String toString() {
		return "Vector4f [x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}

	public boolean equals(Object other){
		return other instanceof Vector4f ? equals((Vector4f) other) : false;
	}
	
	public boolean equals(Vector4f other){
		return super.equals(other) && w == other.w;
	}
	
	public Vector4f copy(){
		return new Vector4f(getX(), getY(), getZ(), getW());
	}
}
