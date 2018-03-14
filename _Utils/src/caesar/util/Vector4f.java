package caesar.util;

public class Vector4f extends Vector3f{

	private float w;
	
	public Vector4f(){
		super();
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
		super(x, y, z);
		this.w = w;
	}

	public float getW(){
		return w;
	}
	
	public void setW(float w){
		this.w = w;
	}
	
	public void set(float x, float y, float z, float w){
		super.set(x, y, z);
		this.w = w;
	}
	
	public static Vector4f from(String[] args){
		float x = Float.parseFloat(args[0]);
		float y = Float.parseFloat(args[1]);
		float z = Float.parseFloat(args[2]);
		float w = Float.parseFloat(args[3]);
		return new Vector4f(x, y, z, w);
	}
	
	public String toString(){
		return "(" + getX() + ", " + getY() + ", " + getZ() + ", " + getW() + ")";
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
