package game.common.util;

public class MathHelper {

	public static int mod(int a, int b){
		int rem = a % b;
		if(rem < 0){
			rem += b;
		}
		return rem;
	}
}
