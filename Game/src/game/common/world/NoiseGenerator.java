/*package game.common.world;

import java.util.Random;

public class NoiseGenerator {
	
	public static float[][] generateWhiteNoise(Random rand, int x, int y, int width, int height){
		float[][] noise = new float[width][height];

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				noise[i][j] = (float)rand.nextDouble();
			}
		}
		return noise;
	}

	public static float[][] generateSmoothNoise(Random rand, float[][] baseNoise, int octave){
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][] noise = new float[width][height];

		int samplePeriod = 1 << octave;
		float sampleFrequency = 1.0f / samplePeriod;

		for(int i = 0; i < width; i++){
			int si0 = (i / samplePeriod) * samplePeriod;
			int si1 = (si0 + samplePeriod) % width;
			float horBlend = (i - si0) * sampleFrequency;

			for(int j = 0; j < height; j++){
				int sj0 = (j / samplePeriod) * samplePeriod;
				int sj1 = (sj0 + samplePeriod) % height;
				float verBlend = (j - sj0) * sampleFrequency;

				float top = interpolate(baseNoise[si0][sj0], baseNoise[si1][sj0], horBlend);

				float bottom = interpolate(baseNoise[si0][sj1], baseNoise[si1][sj1], horBlend);

				noise[i][j] = interpolate(top, bottom, verBlend);
			}
		}

		return noise;
	}

	public static float[][] generatePerlinNoise(Random rand, float[][] baseNoise, int octaves){
		int width = baseNoise.length;
		int height = baseNoise[0].length;

		float[][][] smoothNoise = new float[octaves][width][height];
		float persistance = 0.5f;

		for(int i = 0; i < octaves; i++){
			smoothNoise[i] = generateSmoothNoise(rand, baseNoise, i);
		}

		float[][] perlinNoise = new float[width][height];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		for (int octave = octaves - 1; octave >= 0; octave--)
		{
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int i = 0; i < width; i++)
			{
				for (int j = 0; j < height; j++)
				{
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}

		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				perlinNoise[i][j] /= totalAmplitude;
			}
		}

		return perlinNoise;
	}

	public static float interpolate(float x0, float x1, float alpha){
		return (1 - alpha) * x0 + alpha * x1;
	}
	
	public static float[][] generatePerlinNoise(Random rand, int x, int y, int width, int height, int octaves){
		float[][] whiteNoise = generateWhiteNoise(rand, x, y, width, height);
		float[][] perlinNoise = generatePerlinNoise(rand, whiteNoise, octaves);
		return perlinNoise;
	}
	
	public static float[][] generateSmoothNoise(Random rand, int x, int y, int width, int height){
		float[][] whiteNoise = generateWhiteNoise(rand, x, y, width, height);
		float[][] smoothNoise = generatePerlinNoise(rand, whiteNoise, 1);
		return smoothNoise;
	}
}*/
