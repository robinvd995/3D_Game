package game.common.world.cluster;

public class ClusterColumn {

	private Cluster[] clusters;
	
	public ClusterColumn(int height){
		this.clusters = new Cluster[height];
	}
	
	public void addCluster(int index, Cluster cluster){
		clusters[index] = cluster;
	}
	
	public int getHeight(){
		return clusters.length;
	}
	
	public Cluster getCluster(int index){
		return clusters[index];
	}
}
