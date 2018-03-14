package game.common.util;

import caesar.util.Vector3f;

public class BlockPos {
	
	private int x;
	private int y;
	private int z;
	
	public BlockPos(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockPos move(EnumDirection dir){
		return move(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
	
	public BlockPos move(int dx, int dy, int dz){
		return new BlockPos(x + dx, y + dy, z + dz);
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public Vector3f toVector(){
		return new Vector3f(x, y, z);
	}

	@Override
	public String toString() {
		return "BlockPos [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
