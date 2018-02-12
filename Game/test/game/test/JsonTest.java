package game.test;

import java.util.HashMap;

import org.junit.Test;

import com.google.gson.Gson;

import game.block.Block;
import game.renderer.block.BlockRenderData;
import game.renderer.block.BlockRenderData.BlockRenderPart;

public class JsonTest {

	@Test
	public void testBlockRenderJson(){
		/*BlockRenderData brd = new BlockRenderData();
		BlockRenderPart brp0 = new BlockRenderPart();
		BlockRenderPart brp1 = new BlockRenderPart();
		brd.model = "testmodel";
		brp0.texture = "test0";
		brp1.texture = "test1";
		brd.parts = new HashMap<String,BlockRenderPart>();
		brd.parts.put("up",brp0);
		brd.parts.put("down",brp1);
		
		Gson gson = new Gson();
		String json = gson.toJson(brd);
		System.out.println(json);
		
		BlockRenderData data = gson.fromJson(json, BlockRenderData.class);
		System.out.println(data);*/
		BlockRenderData data = BlockRenderData.from(Block.GRASS);
		System.out.println(data);
	}
}
