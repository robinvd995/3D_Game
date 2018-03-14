package game.common.world.cluster;

import game.common.block.Block;
import game.common.util.BlockPos;
import game.common.world.World;

public class Cluster {

	public static final int CLUSTER_SIZE = 16;
	
	private final World world;
	private final ClusterPosition clusterPosition;
	private Block[][][] blocks;
	
	public Cluster(World world, int clusterPosX, int clusterPosY, int clusterPosZ){
		this.world = world;
		this.clusterPosition = new ClusterPosition(clusterPosX, clusterPosY, clusterPosZ);
		this.blocks = getEmptyBlockArray();
	}
	
	private static Block[][][] getEmptyBlockArray(){
		Block[][][] blockArray = new Block[CLUSTER_SIZE][CLUSTER_SIZE][CLUSTER_SIZE];
		return blockArray;
	}
	
	public void setBlock(Block block, BlockPos pos){
		int x = pos.getX() - clusterPosition.getPosX() * Cluster.CLUSTER_SIZE;
		int y = pos.getY() - clusterPosition.getPosY() * Cluster.CLUSTER_SIZE;
		int z = pos.getZ() - clusterPosition.getPosZ() * Cluster.CLUSTER_SIZE;
		setBlock(block, x, y, z);
	}
	
	public void setBlock(Block block, int x, int y, int z){
		blocks[x][y][z] = block;
	}
	
	public Block getBlock(BlockPos pos){
		int x = pos.getX() - clusterPosition.getPosX() * Cluster.CLUSTER_SIZE;
		int y = pos.getY() - clusterPosition.getPosY() * Cluster.CLUSTER_SIZE;
		int z = pos.getZ() - clusterPosition.getPosZ() * Cluster.CLUSTER_SIZE;
		return getBlock(x, y, z);
	}
	
	public Block getBlock(int x, int y, int z){
		return blocks[x][y][z];
	}
	
	public World getWorld(){
		return world;
	}
	
	public ClusterPosition getPosition(){
		return clusterPosition;
	}
}
