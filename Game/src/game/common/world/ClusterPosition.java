package game.common.world;

public class ClusterPosition {

	private final int posX;
	private final int posY;
	private final int posZ;
	
	public ClusterPosition(int x, int y, int z){
		this.posX = x;
		this.posY = y;
		this.posZ = z;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + posX;
		result = prime * result + posY;
		result = prime * result + posZ;
		return result;
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
