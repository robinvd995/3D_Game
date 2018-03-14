package game.common.block;

import caesar.util.Vector3f;
import game.common.physics.AxisAlignedBB;
import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;

public class Block {

	public static final int BLOCK_AMOUNT = 6;
	
	private static final Block[] BLOCKS = new Block[BLOCK_AMOUNT];
	
	public static final Block AIR = new BlockAir(0).setUnlocalizedName("air");
	public static final Block GRASS = new Block(1).setUnlocalizedName("grass");
	public static final Block WATER = new BlockWater(2).setUnlocalizedName("water");
	public static final Block STONE = new Block(3).setUnlocalizedName("stone");
	public static final Block SAND = new Block(4).setUnlocalizedName("sand");
	public static final Block GOLD_ORE = new Block(5).setUnlocalizedName("gold_ore");
	
	private Vector3f blockColor = new Vector3f(1.0f, 1.0f, 1.0f);
	
	private boolean needsRendering = true;
	
	private final int blockId;
	private String unlocalizedName;
	
	private AxisAlignedBB bounds = new AxisAlignedBB(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	
	public Block(int id){
		blockId = id;
		BLOCKS[id] = this;
	}
	
	public Block setBlockColor(float r, float g, float b){
		this.blockColor = new Vector3f(r, g, b);
		return this;
	}
	
	public Vector3f getBlockColor(World world, BlockPos pos){
		return blockColor;
	}
	
	public Block disableRendering(){
		this.needsRendering = false;
		return this;
	}
	
	public final boolean needsRendering(){
		return needsRendering;
	}
	
	public boolean shouldBlockBeRendered(World world, BlockPos pos){
		if(!needsRendering){
			return false;
		}
		for(EnumDirection dir : EnumDirection.getValidDirections()){
			if(shouldRenderSide(world, pos, dir)) return true;
		}
		return false;
	}
	
	public int getBlockId(){
		return blockId;
	}
	
	public boolean isFullCube(){
		return true;
	}
	
	public boolean isTransparantBlock(){
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blockId;
		return result;
	}
	
	public void setBounds(float minx, float miny, float minz, float maxx, float maxy, float maxz){
		this.bounds = new AxisAlignedBB(minx, miny, minz, maxx, maxy, maxz);
	}
	
	public AxisAlignedBB getBounds(World world, BlockPos pos){
		return bounds;
	}
	
	public static Block getBlockFromId(int id){
		return id < BLOCKS.length ? BLOCKS[id] : AIR;
	}
	
	public String getUnlocalizedName(){
		return unlocalizedName;
	}
	
	public Block setUnlocalizedName(String unlocalizedName){
		this.unlocalizedName = unlocalizedName;
		return this;
	}
	
	@Override
	public String toString() {
		return "Block [blockId=" + blockId + ", unlocalizedName=" + unlocalizedName + "]";
	}

	public boolean shouldRenderSide(World world, BlockPos pos, EnumDirection side){
		Block block = world.getBlock(pos.move(side));
		return !block.isFullCube() || block.isTransparantBlock();
	}
}
