package game.client.renderer.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import caesar.util.Vector3f;
import game.client.renderer.DebugRenderer;
import game.client.renderer.debug.DebugTransform;
import game.common.block.Block;
import game.common.entity.Entity;
import game.common.entity.Player;
import game.common.util.BlockPos;
import game.common.world.World;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterPosition;

public class WorldClient extends World{

	private Queue<Cluster> clustersToAdd = new ConcurrentLinkedQueue<Cluster>();
	private Queue<Cluster> clustersToRemove = new ConcurrentLinkedQueue<Cluster>();

	private HashMap<ClusterPosition,ClusterRenderData> clusterRenderMap = new HashMap<ClusterPosition,ClusterRenderData>();

	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);
	
	private HashMap<Class<? extends Entity>,List<Entity>> entityRenderMap = new HashMap<Class<? extends Entity>,List<Entity>>();
	
	private ClusterPosition lastPosition;
	
	private Player thePlayer;
	
	public WorldClient(Player player){
		super();
		thePlayer = player;
	}

	private void createClusterRenderData(Cluster cluster){
		ClusterRenderData crd = ClusterRenderData.createFromCluster(cluster);
		clusterRenderMap.put(cluster.getPosition(), crd);
	}

	@Override
	public void update(double delta){
		super.update(delta);
		DebugRenderer.INSTANCE.addObjectToRender(new DebugTransform(new Vector3f(0.0f, 50.0f, 0.0f), worldProvider.getLightDirection(getTimeOfDay())));
		
		ClusterPosition cp = thePlayer.getClusterCoords();
		if(!cp.equals(lastPosition)){
			
			int minX = cp.getPosX() - CLUSTER_LOAD_DISTANCE;
			int minY = Math.max(cp.getPosY() - CLUSTER_LOAD_DISTANCE, 0);
			int minZ = cp.getPosZ() - CLUSTER_LOAD_DISTANCE;
			int maxX = cp.getPosX() + CLUSTER_LOAD_DISTANCE;
			int maxY = Math.min(cp.getPosY() + CLUSTER_LOAD_DISTANCE, worldProvider.getWorldHeight() - 1);
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

	@Override
	public void fixedUpdate(double delta){
		super.fixedUpdate(delta);
		
		lightDirection = worldProvider.getLightDirection(getTimeOfDay());
		lightColor = worldProvider.getLightColor(getTimeOfDay());
	}

	@Override
	protected void onClusterLoaded(Cluster cluster) {
		super.onClusterLoaded(cluster);
		clustersToAdd.add(cluster);
	}

	@Override
	protected void onClusterUnloaded(Cluster cluster) {
		super.onClusterUnloaded(cluster);
		clustersToRemove.add(cluster);
	}

	@Override
	protected void onAllClustersChanged(){

		while(!clustersToRemove.isEmpty()){
			Cluster cluster = clustersToRemove.poll();
			ClusterPosition pos = cluster.getPosition();
			ClusterRenderData data = clusterRenderMap.get(pos);
			data.onUnloaded();
			clusterRenderMap.remove(pos);
		}

		while(!clustersToAdd.isEmpty()){
			Cluster cluster = clustersToAdd.poll();
			createClusterRenderData(cluster);
		}
	}

	@Override
	protected void onBlockChanged(Block block, BlockPos pos) {
		super.onBlockChanged(block, pos);
	}

	public Set<ClusterPosition> getClusterRenderPositions(){
		return clusterRenderMap.keySet();
	}

	public ClusterRenderData getClusterRenderData(ClusterPosition pos){
		return clusterRenderMap.get(pos);
	}

	public Vector3f getLightDirection(){
		return lightDirection;
	}

	public Vector3f getLightColor(){
		return lightColor;
	}

	public float getAmbientStrength(){
		return worldProvider.getAmbientStrenght();
	}
	
	@Override
	protected void onEntitySpawned(Entity entity){
		Class<? extends Entity> clzz = entity.getClass();
		if(!entityRenderMap.containsKey(clzz)){
			entityRenderMap.put(clzz, new ArrayList<Entity>());
		}
		
		List<Entity> list = entityRenderMap.get(clzz);
		list.add(entity);
	}
	
	@Override
	protected void onEntityRemoved(Entity entity){
		Class<? extends Entity> clzz = entity.getClass();
		if(!entityRenderMap.containsKey(clzz)){
			return;
		}
		
		List<Entity> list = entityRenderMap.get(clzz);
		list.remove(entity);
	}
	
	public Set<Class<? extends Entity>> getEntityClassesToRender(){
		return entityRenderMap.keySet();
	}
	
	public List<Entity> getEntityInstancesForClass(Class<? extends Entity> clzz){
		return entityRenderMap.get(clzz);
	}
}
