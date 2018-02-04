package game.test;

import game.util.SimplexNoise;

public class SimplexNoiseTest {

	public static void main(String[] args){
		
		SimplexNoise noise = new SimplexNoise(8, 1.0d, 10);
		
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				for(int k = 0; k < 8; k++){
					System.out.println(noise.getNoise(i, j, k));
				}
			}
		}
	}
}
