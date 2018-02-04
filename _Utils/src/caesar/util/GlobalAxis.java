package caesar.util;


public enum GlobalAxis {

	INVALID(0.0f, 0.0f, 0.0f),
	X(1.0f, 0.0f, 0.0f),
	Y(0.0f, 1.0f, 0.0f),
	Z(0.0f, 0.0f, 1.0f);
	
	private final float x;
	private final float y;
	private final float z;
	
	private GlobalAxis(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f toVector(){
		return new Vector3f(x, y, z);
	}
	
	public Vector3f toVector(float amount){
		return new Vector3f(x * amount, y * amount, z * amount);
	}
	
	public static GlobalAxis fromVector(float x, float y, float z){
		int amount = 0;
		if(x == 1.0f){
			amount++;
		}
		if(y == 1.0f){
			amount++;
		}
		if(z == 1.0f){
			amount++;
		}
		if(amount != 1){
			return INVALID;
		}else{
			if(x == 1.0f){
				return X;
			}
			if(y == 1.0f){
				return Y;
			}
			if(x == 1.0f){
				return Z;
			}
			return INVALID;
		}
	}
}
