package game.world;

import java.util.Random;

import game.block.Block;

public class WorldGenerator {

	public World generateWorld(int width, int height, int minHeight, int maxHeight, int waterLevel){
		Random rand = new Random();
		int amplitude = maxHeight - minHeight;
		float[][] perlinNoise = NoiseGenerator.generatePerlinNoise(rand, width, height, 4);

		World world = new World(width, height);

		for(int x = 0; x < width; x++){
			for(int z = 0; z < height; z++){
				float value = perlinNoise[x][z];
				int blockPosY = (int) (minHeight + (value * amplitude));
				for(int y = 0; y < world.getMaxY(); y++){
					Block blockToSet = Block.AIR;
					if(y == blockPosY){
						blockToSet = Block.GRASS;
					}
					else if(y > blockPosY && y <= waterLevel){
						blockToSet = Block.WATER;
					}
					else if(y < blockPosY){
						blockToSet = Block.STONE;
					}
					world.setBlock(blockToSet, x, y, z);
				}
			}
		}

		return world;
	}

	public World generateFlatWorld(int width, int height){
		World world = new World(width, height);

		for(int x = 0; x < width; x++){
			for(int z = 0; z < height; z++){
				for(int y = 0; y < world.getMaxY(); y++){
					Block blockToSet = Block.AIR;
					if(y == 0 || (x == 4 && z == 4)){
						blockToSet = Block.STONE;
					}
					world.setBlock(blockToSet, x, y, z);
				}
			}
		}
		
		return world;
	}
}
