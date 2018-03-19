package game.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.common.block.Block;
import game.common.entity.Player;
import game.common.util.BlockPos;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterPosition;

public class World {
	
	private static final int CLUSTER_LOAD_DISTANCE = 4;
	
	private Player player;
	
	private HashMap<ClusterPosition,Cluster> clusterMap = new HashMap<ClusterPosition,Cluster>();
	
	private WorldProvider worldProvider;
	
	private ClusterPosition lastPosition;
	
	public World(){
		worldProvider = new WorldProviderOverworld();
		
		player = new Player(this);
		player.getTransform().getPosition().set(0, 8, 0);
		player.init();
	}
	
	/**
	 * Sets the block at the given position
	 * @param block the block to set to
	 * @param pos the position of the block
	 */
	public void setBlock(Block block, BlockPos pos){
		ClusterPosition clusterPos = new ClusterPosition(pos);
		Cluster cluster = getCluster(clusterPos);
		if(cluster == null) throw new RuntimeException("Trying to set a block in a custer that does not exist!");
		cluster.setBlock(block, pos);
		onBlockChanged(block, pos);
	}
	
	/**
	 * Gets the block at the given position
	 * @param pos the position of the block
	 * @return the block at the given position
	 */
	public Block getBlock(BlockPos pos){
		ClusterPosition clusterPos = new ClusterPosition(pos);
		Cluster cluster = getCluster(clusterPos);
		if(cluster == null) return Block.AIR;
		//System.out.println(pos + "," + cluster.getPosition());
		return cluster.getBlock(pos);
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void update(double delta){
		player.update(delta);
		player.lateUpdate(delta);
		
		ClusterPosition cp = player.getClusterCoords();
		if(!player.getClusterCoords().equals(lastPosition)){
			
			int minX = cp.getPosX() - CLUSTER_LOAD_DISTANCE;
			int minY = Math.max(cp.getPosY() - CLUSTER_LOAD_DISTANCE, 0);
			int minZ = cp.getPosZ() - CLUSTER_LOAD_DISTANCE;
			int maxX = cp.getPosX() + CLUSTER_LOAD_DISTANCE;
			int maxY = Math.min(cp.getPosY() + CLUSTER_LOAD_DISTANCE, worldProvider.getWorldHeight());
			int maxZ = cp.getPosZ() + CLUSTER_LOAD_DISTANCE;
			
			List<ClusterPosition> clustersToRemove = new ArrayList<ClusterPosition>();
			clustersToRemove.addAll(clusterMap.keySet());
			
			for(int i = minX; i <= maxX; i++){
				for(int j = minY; j <= maxY; j++){
					for(int k = minZ; k <= maxZ; k++){
						
						ClusterPosition clusterPos = new ClusterPosition(i, j, k);
						
						if(!clusterMap.containsKey(clusterPos)){
							Cluster cluster = worldProvider.getCluster(this, i, j, k);
							clusterMap.put(clusterPos, cluster);
							onClusterLoaded(cluster);
						}
						
						clustersToRemove.remove(clusterPos);
					}
				}
			}
			
			for(ClusterPosition clusterPos : clustersToRemove){
				Cluster cluster = clusterMap.get(clusterPos);
				if(cluster == null) throw new RuntimeException("Cluster map does not contain the cluster to remove, this is an bug!");
				clusterMap.remove(clusterPos);
				onClusterUnloaded(cluster);
			}
			
			lastPosition = cp;
			
			onAllClustersChanged();
		}
	}
	
	public void fixedUpdate(double delta){
		
	}
	
	private Cluster getCluster(ClusterPosition pos){
		return clusterMap.get(pos);
	}
	
	protected void onClusterLoaded(Cluster cluster) {}
	
	protected void onClusterUnloaded(Cluster cluster) {}
	
	protected void onBlockChanged(Block block, BlockPos pos) {}
	
	protected void onAllClustersChanged(){}
}
