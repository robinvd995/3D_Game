package game.common.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import game.common.block.Block;
import game.common.entity.Entity;
import game.common.entity.Player;
import game.common.util.BlockPos;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterPosition;

public class World {
	
	public static final int CLUSTER_LOAD_DISTANCE = 4;
	
	//private Player player;
	
	protected HashMap<ClusterPosition,Cluster> clusterMap = new HashMap<ClusterPosition,Cluster>();
	
	protected WorldProvider worldProvider;
	
	private List<Entity> entities = new ArrayList<Entity>();
	private Queue<Entity> entitiesToRemove = new LinkedList<Entity>();
	
	private double timeOfDay;
	
	public World(){
		worldProvider = new WorldProviderOverworld();
		timeOfDay = worldProvider.getDayLength() / 8;
	}
	
	public void init(){
		//spawnEntity(player);
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
	
	public void update(double delta){
		//player.update(delta);
		//player.lateUpdate(delta);
		
		for(Entity entity : entities){
			entity.update(delta);
		}
		
		for(Entity entity : entities){
			entity.lateUpdate(delta);
		}
		
		while(!entitiesToRemove.isEmpty()){
			Entity entity = entitiesToRemove.poll();
			entities.remove(entity);
			onEntityRemoved(entity);
		}
	}
	
	public void fixedUpdate(double delta){
		timeOfDay = (timeOfDay + delta) % worldProvider.getDayLength();
	}
	
	private Cluster getCluster(ClusterPosition pos){
		return clusterMap.get(pos);
	}
	
	
	public void spawnEntity(Entity entity){
		entities.add(entity);
		entity.onEntitySpawned(this);
		onEntitySpawned(entity);
	}
	
	public void removeEntity(Entity entity){
		entitiesToRemove.add(entity);
	}
	
	protected void onClusterLoaded(Cluster cluster) {}
	
	protected void onClusterUnloaded(Cluster cluster) {}
	
	protected void onBlockChanged(Block block, BlockPos pos) {}
	
	protected void onAllClustersChanged() {}
	
	protected void onEntitySpawned(Entity entity) {}
	
	protected void onEntityRemoved(Entity entity) {}
	
	public double getTimeOfDay(){
		return timeOfDay;
	}

	public float getGravity() {
		return worldProvider.getGravity();
	}
}
