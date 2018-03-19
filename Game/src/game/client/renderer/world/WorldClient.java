package game.client.renderer.world;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import caesar.util.Vector3f;
import caesar.util.interpolate.Interpolator;
import game.client.renderer.DebugRenderer;
import game.client.renderer.debug.DebugTransform;
import game.common.block.Block;
import game.common.util.BlockPos;
import game.common.world.World;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterPosition;

public class WorldClient extends World{

	private static final double MAX_TIME_OF_DAY = 100.0d;

	private Queue<Cluster> clustersToAdd = new ConcurrentLinkedQueue<Cluster>();
	private Queue<Cluster> clustersToRemove = new ConcurrentLinkedQueue<Cluster>();

	private HashMap<ClusterPosition,ClusterRenderData> clusterRenderMap = new HashMap<ClusterPosition,ClusterRenderData>();

	private double timeOfDay = 0;
	private Vector3f lightDirection = new Vector3f(0.0f, 0.0f, 0.0f);
	private Vector3f lightColor = new Vector3f(1.0f, 1.0f, 1.0f);
	private float ambientStrength = 0.3f;
	
	private Interpolator<Vector3f> lightColorInterpolator = new Interpolator<Vector3f>(MAX_TIME_OF_DAY);
	
	public WorldClient(){
		super();
		//Sunrise
		lightColorInterpolator.addInterpolation(00, new Vector3f(0.0f, 0.0f, 0.0f));
		lightColorInterpolator.addInterpolation(05, new Vector3f(1.0f, 0.5f, 0.0f));
		lightColorInterpolator.addInterpolation(10, new Vector3f(1.0f, 1.0f, 1.0f));
		
		//Sunset
		lightColorInterpolator.addInterpolation(40, new Vector3f(1.0f, 1.0f, 1.0f));
		lightColorInterpolator.addInterpolation(45, new Vector3f(1.0f, 0.5f, 0.0f));
		lightColorInterpolator.addInterpolation(50, new Vector3f(0.0f, 0.0f, 0.0f));
	}

	private void createClusterRenderData(Cluster cluster){
		ClusterRenderData crd = ClusterRenderData.createFromCluster(cluster);
		clusterRenderMap.put(cluster.getPosition(), crd);
	}

	@Override
	public void update(double delta){
		super.update(delta);
		DebugRenderer.INSTANCE.addObjectToRender(new DebugTransform(new Vector3f(0.0f, 50.0f, 0.0f), lightDirection));
	}

	@Override
	public void fixedUpdate(double delta){
		timeOfDay = (timeOfDay + delta) % MAX_TIME_OF_DAY;

		float lightRotation = (float) ((timeOfDay / MAX_TIME_OF_DAY) * Math.PI * 2);
		
		float lightRotX = (float) Math.cos(lightRotation);
		float lightRotY = (float) Math.sin(lightRotation);

		lightRotation += (1.0f * delta) % (Math.PI * 2);

		lightDirection.set(lightRotX, lightRotY, 0.0f);
		lightColor = lightColorInterpolator.getInterpolatedValue(timeOfDay);
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
		return ambientStrength;
	}
}
