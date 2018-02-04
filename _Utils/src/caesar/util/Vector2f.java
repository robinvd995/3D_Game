package caesar.util;

public class Vector2f {

	private float x;
	private float y;
	
	public Vector2f(){
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setX(float x){
		this.x = x;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public static Vector2f from(String[] args){
		float x = Float.parseFloat(args[0]);
		float y = Float.parseFloat(args[1]);
		return new Vector2f(x, y);
	}
	
	public String toString(){
		return "(" + getX() + ", " + getY() + ")";
	}
	
	public boolean equals(Object other){
		return other instanceof Vector2f ? equals((Vector2f) other) : false;
	}
	
	public boolean equals(Vector2f other){
		return x == other.x && y == other.y;
	}
	
	public Vector2f copy(){
		return new Vector2f(getX(), getY());
	}
	
	public Vector2f translate(float x, float y){
		this.x += x;
		this.y += y;
		return this;
	}
	
	public Vector2f translate(Vector2f vector){
		return translate(vector.getX(), vector.getY());
	}
	
	public float distanceTo(Vector2f other){
		return (float) Math.hypot(x - other.x, y - other.y);
	}
	
	public Vector2f set(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
}
