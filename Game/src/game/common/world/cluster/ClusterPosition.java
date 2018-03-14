package game.common.world.cluster;

import game.common.util.BlockPos;

public class ClusterPosition {

	private final int posX;
	private final int posY;
	private final int posZ;
	
	public ClusterPosition(int x, int y, int z){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
	}
	
	public ClusterPosition(BlockPos pos){
		this.posX = Math.floorDiv(pos.getX(), Cluster.CLUSTER_SIZE);
		this.posY = Math.floorDiv(pos.getY(), Cluster.CLUSTER_SIZE);
		this.posZ = Math.floorDiv(pos.getZ(), Cluster.CLUSTER_SIZE);
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public int getPosZ(){
		return posZ;
	}

	public BlockPos getWorldCoords(int x, int y, int z){
		int nx = (posX * Cluster.CLUSTER_SIZE) + x;
		int ny = (posY * Cluster.CLUSTER_SIZE) + y;
		int nz = (posZ * Cluster.CLUSTER_SIZE) + z;
		return new BlockPos(nx, ny, nz);
	}
	
	@Override
	public String toString() {
		return "ClusterPosition [posX=" + posX + ", posY=" + posY + ", posZ=" + posZ + "]";
	}

	@Override
	public int hashCode() {
		/*final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		result = prime * result + posZ;
		return result;*/
		String hashString = posX + "," + posY + "," + posZ;
		return hashString.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ClusterPosition)) {
			return false;
		}
		ClusterPosition other = (ClusterPosition) obj;
		if (posX != other.posX) {
			return false;
		}
		if (posY != other.posY) {
			return false;
		}
		if (posZ != other.posZ) {
			return false;
		}
		return true;
	}
}
