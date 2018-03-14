package game.common.world;

import java.util.Random;

import game.common.block.Block;
import game.common.world.cluster.Cluster;

public class WorldGenerator {

	private Random rand;
	private int minHeight;
	private int maxHeight;
	private int waterLevel;
	
	public WorldGenerator(int minHeight, int maxHeight, int waterLevel){
		this.rand = new Random();
		this.minHeight = minHeight;
		this.maxHeight = maxHeight;
		this.waterLevel = waterLevel;
	}
	
	public Cluster generateCluster(World world, int clusterX, int clusterY, int clusterZ){
		Cluster cluster = new Cluster(world, clusterX, clusterY, clusterZ);
		
		int cx = clusterX * Cluster.CLUSTER_SIZE;
		int cz = clusterZ * Cluster.CLUSTER_SIZE;
		
		int amp = maxHeight - minHeight;
		float[][] perlinNoise = NoiseGenerator.generatePerlinNoise(rand, cx, cz, Cluster.CLUSTER_SIZE, Cluster.CLUSTER_SIZE, 5);
		
		for(int x = 0; x < Cluster.CLUSTER_SIZE; x++){
			for(int z = 0; z < Cluster.CLUSTER_SIZE; z++){
				float value = perlinNoise[x][z];
				int blockPosY = (int) (minHeight + (value * amp));
				for(int y = 0; y < Cluster.CLUSTER_SIZE; y++){
					Block blockToSet = Block.AIR;
					if(y == blockPosY){
						if(y <= waterLevel){
							blockToSet = Block.SAND;
						}
						else{
							blockToSet = Block.GRASS;
						}
					}
					else if(y > blockPosY && y <= waterLevel){
						blockToSet = Block.WATER;
					}
					else if(y < blockPosY){
						blockToSet = Block.STONE;
					}
					cluster.setBlock(blockToSet, x, y, z);
				}
			}
		}
		
		return cluster;
	}
	
	/*public World generateWorld(int width, int height, int minHeight, int maxHeight, int waterLevel){
		Random rand = new Random();
		int amplitude = maxHeight - minHeight;
		float[][] perlinNoise = NoiseGenerator.generatePerlinNoise(rand, width, height, 5);

		World world = new World(width, height);

		for(int x = 0; x < width; x++){
			for(int z = 0; z < height; z++){
				float value = perlinNoise[x][z];
				int blockPosY = (int) (minHeight + (value * amplitude));
				for(int y = 0; y < world.getMaxY(); y++){
					Block blockToSet = Block.AIR;
					if(y == blockPosY){
						if(y <= waterLevel){
							blockToSet = Block.SAND;
						}
						else{
							blockToSet = Block.GRASS;
						}
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
	}*/
}
