package game.world;

import game.block.Block;
import game.entity.Player;
import game.util.BlockPos;

public class World {
	
	private Player player;
	
	private Block[][][] blocks;
	
	public World(int w, int h){
		blocks = new Block[w][64][h];
		player = new Player(this);
		player.getTransform().getPosition().set(0, 20, 0);
		//player.getTransform().setScale(1.0f, 2.0f, 1.0f);
		player.init();
	}
	
	public void setBlock(Block block, int x, int y, int z){
		blocks[x][y][z] = block;
	}
	
	public Block getBlock(BlockPos pos){
		//Temp
		if((pos.getX() < 0 || pos.getX() >= getMaxX() - 1) || (pos.getY() < 0 || pos.getY() >= getMaxY() - 1) || (pos.getZ() < 0 || pos.getZ() >= getMaxZ() - 1)){
			return Block.AIR;
		}
		return blocks[pos.getX()][pos.getY()][pos.getZ()];
	}
	
	public int getMaxX(){
		return blocks.length;
	}
	
	public int getMaxY(){
		return blocks[0].length;
	}
	
	public int getMaxZ(){
		return blocks[0][0].length;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public void update(double delta){
		player.update(delta);
		player.lateUpdate(delta);
	}
	
}
