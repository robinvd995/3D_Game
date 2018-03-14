package game.common.world.cluster;

import game.common.world.World;
import game.common.world.WorldGenerator;

public class ClusterProvider {

	private final World world;
	
	public ClusterProvider(World world){
		this.world = world;
	}
	
	public Cluster generateCluster(ClusterPosition pos){
		WorldGenerator generator = world.getWorldGenerator();
		return generator.generateCluster(world, pos.getPosX(), pos.getPosY(), pos.getPosZ());
	}
}
