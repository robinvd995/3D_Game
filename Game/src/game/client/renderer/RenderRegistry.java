package game.client.renderer;

import java.util.HashMap;

import game.client.renderer.block.BlockRenderData;
import game.client.renderer.block.BlockRendererCube;
import game.client.renderer.block.BlockRendererCubeSided;
import game.client.renderer.block.IBlockRenderer;
import game.client.renderer.item.IItemRenderer;
import game.client.renderer.item.ItemRendererBow;
import game.client.renderer.item.ItemRendererPickaxe;
import game.client.renderer.item.ItemRendererSword;
import game.common.block.Block;
import game.common.item.Item;

public class RenderRegistry {

	private static HashMap<String,IBlockRenderer> blockRenderMap;
	private static BlockRenderData[] blockRenderDataArray;
	
	private static IItemRenderer[] itemRenderers;
	
	protected static void loadBlockRenderData(){
		blockRenderDataArray = new BlockRenderData[Block.BLOCK_AMOUNT];
		
		for(int i = 0; i < Block.BLOCK_AMOUNT; i++){
			Block block = Block.getBlockFromId(i);
			if(block != null && block.needsRendering()){
				blockRenderDataArray[i] = BlockRenderData.from(block);
				System.out.println(blockRenderDataArray[i]);
			}
			else{
				System.out.println("Block " + block.getUnlocalizedName() + " does not need rendering, skipping loading block render data!");
			}
		}
	}
	
	public static BlockRenderData getBlockRenderData(Block block){
		return getBlockRenderData(block.getBlockId());
	}
	
	public static BlockRenderData getBlockRenderData(int blockid){
		return blockRenderDataArray[blockid];
	}
	
	protected static void registerBlockRenderers(){
		blockRenderMap = new HashMap<String,IBlockRenderer>();
		
		registerBlockRenderer(new BlockRendererCube());
		registerBlockRenderer(new BlockRendererCubeSided());
	}
	
	public static void registerBlockRenderer(IBlockRenderer renderer){
		blockRenderMap.put(renderer.renderId(), renderer);
	}
	
	public static IBlockRenderer getBlockRenderer(String id){
		return blockRenderMap.get(id);
	}
	
	public static void registerItemRenderer(Item item, IItemRenderer renderer){
		itemRenderers[item.getItemId()] = renderer;
	}
	
	public static IItemRenderer getItemRenderer(Item item){
		return itemRenderers[item.getItemId()];
	}
	
	protected static void registerItemRenderers(){
		itemRenderers = new IItemRenderer[Item.ITEM_AMOUNT];
		registerItemRenderer(Item.PICKAXE, new ItemRendererPickaxe());
		registerItemRenderer(Item.SWORD, new ItemRendererSword());
		registerItemRenderer(Item.BOW, new ItemRendererBow());
	}
}
