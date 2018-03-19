package game.common.world;

import caesar.util.Vector3f;
import caesar.util.interpolate.Interpolator;
import game.common.CommonHandler;
import game.common.EnumSide;
import game.common.block.Block;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterColumn;

public class WorldProviderOverworld extends WorldProvider{
	
	private static final int MIN_HEIGHT = 10;
	private static final int MAX_HEIGHT = 20;
	private static final int WATER_LEVEL = 15;
	
	private Interpolator<Vector3f> lightColorInterpolator;

	private Vector3f lightDirection;

	public WorldProviderOverworld(){
		if(CommonHandler.instance().getSide() == EnumSide.CLIENT){
			lightDirection = new Vector3f();
			lightColorInterpolator = new Interpolator<Vector3f>(getDayLength());

			//Sunrise
			lightColorInterpolator.addInterpolation(00, new Vector3f(0.3f, 0.3f, 0.3f));
			lightColorInterpolator.addInterpolation(05, new Vector3f(1.0f, 0.5f, 0.0f));
			lightColorInterpolator.addInterpolation(10, new Vector3f(1.0f, 1.0f, 1.0f));

			//Sunset
			lightColorInterpolator.addInterpolation(40, new Vector3f(1.0f, 1.0f, 1.0f));
			lightColorInterpolator.addInterpolation(45, new Vector3f(1.0f, 0.5f, 0.0f));
			lightColorInterpolator.addInterpolation(50, new Vector3f(0.3f, 0.3f, 0.3f));
		}
	}

	@Override
	public String getWorldName() {
		return "overworld";
	}
	
	@Override
	public int getWorldHeight() {
		return 4;
	}

	@Override
	public double getDayLength() {
		return 100.0d;
	}

	@Override
	public Vector3f getLightColor(double time) {
		return lightColorInterpolator.getInterpolatedValue(time);
	}

	@Override
	public Vector3f getLightDirection(double time) {
		float lightRotation = (float) ((time / getDayLength()) * Math.PI * 2);

		float lightRotX = (float) Math.cos(lightRotation);
		float lightRotY = (float) Math.sin(lightRotation);

		lightDirection.set(lightRotX, lightRotY, 0.0f);
		return lightDirection;
	}

	@Override
	public float getAmbientStrenght() {
		return 0.3f;
	}
	
	@Override
	protected ClusterColumn generateClusterColumn(World world, int clusterX, int clusterZ) {
		int cx = clusterX * Cluster.CLUSTER_SIZE;
		int cz = clusterZ * Cluster.CLUSTER_SIZE;
		
		int amp = MAX_HEIGHT - MIN_HEIGHT;
		float[][] perlinNoise = NoiseGenerator.generatePerlinNoise(rand, cx, cz, Cluster.CLUSTER_SIZE, Cluster.CLUSTER_SIZE, 5);
		
		ClusterColumn column = new ClusterColumn(getWorldHeight());
		for(int i = 0; i < column.getHeight(); i++){
			Cluster cluster = generateCluster(world, perlinNoise, amp, clusterX, i, clusterZ);
			column.addCluster(i, cluster);
		}
		return column;
	}
	
	private Cluster generateCluster(World world, float[][] noise, int amp, int clusterX, int clusterY, int clusterZ){
		
		Cluster cluster = new Cluster(world, clusterX, clusterY, clusterZ);
		
		for(int x = 0; x < Cluster.CLUSTER_SIZE; x++){
			for(int z = 0; z < Cluster.CLUSTER_SIZE; z++){
				float value = noise[x][z];
				int blockPosY = (int) (MIN_HEIGHT + (value * amp));
				for(int y = 0; y < Cluster.CLUSTER_SIZE; y++){
					int posY = y + clusterY * Cluster.CLUSTER_SIZE;
					Block blockToSet = Block.AIR;
					if(posY == blockPosY){
						if(posY <= WATER_LEVEL){
							blockToSet = Block.SAND;
						}
						else{
							blockToSet = Block.GRASS;
						}
					}
					else if(posY > blockPosY && posY <= WATER_LEVEL){
						blockToSet = Block.WATER;
					}
					else if(posY < blockPosY){
						blockToSet = Block.STONE;
					}
					cluster.setBlock(blockToSet, x, y, z);
				}
			}
		}
		
		return cluster;
	}

}
