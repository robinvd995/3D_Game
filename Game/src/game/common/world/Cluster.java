package game.common.world;

import game.common.block.Block;

public class Cluster {

	public static final int CLUSTER_SIZE = 16;
	
	private final World world;
	private final ClusterPosition clusterPosition;
	private Block[][][] blocks;
	
	public Cluster(World world, int clusterPosX, int clusterPosY, int clusterPosZ){
		this.world = world;
		this.clusterPosition = new ClusterPosition(clusterPosX, clusterPosY, clusterPosZ);
	}
}
