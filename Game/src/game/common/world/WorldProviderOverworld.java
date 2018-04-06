package game.common.world;

import java.util.Random;

import caesar.util.Vector3f;
import caesar.util.interpolate.Interpolator;
import game.common.CommonHandler;
import game.common.EnumSide;
import game.common.block.Block;
import game.common.entity.EntityPalmTree;
import game.common.world.FastNoise.NoiseType;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterColumn;

public class WorldProviderOverworld extends WorldProvider{

	private static final int MIN_HEIGHT = 13;
	private static final int MAX_HEIGHT = 20;
	private static final int WATER_LEVEL = 15;

	private Interpolator<Vector3f> lightColorInterpolator;

	private Vector3f lightDirection;

	private FastNoise noiseGenerator;

	private Random rand = new Random();

	public WorldProviderOverworld(){

		noiseGenerator = new FastNoise(1337);
		noiseGenerator.SetNoiseType(NoiseType.Simplex);
		noiseGenerator.SetFractalOctaves(5);

		if(CommonHandler.instance().getSide() == EnumSide.CLIENT){
			lightDirection = new Vector3f();
			lightColorInterpolator = new Interpolator<Vector3f>(getDayLength());

			//Sunrise
			lightColorInterpolator.addInterpolation(getDayLength() * 0.0f, new Vector3f(0.0f, 0.0f, 0.0f));
			lightColorInterpolator.addInterpolation(getDayLength() * 0.05f, new Vector3f(1.0f, 0.5f, 0.0f));
			lightColorInterpolator.addInterpolation(getDayLength() * 0.1f, new Vector3f(1.0f, 1.0f, 1.0f));

			//Sunset
			lightColorInterpolator.addInterpolation(getDayLength() * 0.4f, new Vector3f(1.0f, 1.0f, 1.0f));
			lightColorInterpolator.addInterpolation(getDayLength() * 0.45f, new Vector3f(1.0f, 0.5f, 0.0f));
			lightColorInterpolator.addInterpolation(getDayLength() * 0.5f, new Vector3f(0.0f, 0.0f, 0.0f));
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
		return 10000.0d;
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

		int amp = MAX_HEIGHT - MIN_HEIGHT;

		ClusterColumn column = new ClusterColumn(getWorldHeight());
		for(int i = 0; i < column.getHeight(); i++){
			Cluster cluster = generateCluster(world, amp, clusterX, i, clusterZ);
			column.addCluster(i, cluster);
		}
		return column;
	}

	private Cluster generateCluster(World world, int amp, int clusterX, int clusterY, int clusterZ){

		Cluster cluster = new Cluster(world, clusterX, clusterY, clusterZ);

		int cx = clusterX * Cluster.CLUSTER_SIZE;
		int cz = clusterZ * Cluster.CLUSTER_SIZE;

		for(int x = 0; x < Cluster.CLUSTER_SIZE; x++){
			for(int z = 0; z < Cluster.CLUSTER_SIZE; z++){
				float value = noiseGenerator.GetNoise((cx + x) * 2, (cz + z) * 2);
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
							if(rand.nextInt(10) == 0){
								EntityPalmTree tree = new EntityPalmTree(world);
								float randScale = rand.nextFloat() * 0.1f + 0.45f;
								tree.getTransform().setScale(randScale, randScale, randScale);
								tree.getTransform().setPosition(cx + x + 0.5f, posY + 1.0f, cz + z + 0.5f);
								world.spawnEntity(tree);
							}
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
