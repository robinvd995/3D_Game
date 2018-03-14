package game.client.renderer.world;

import game.client.renderer.RenderRegistry;
import game.client.renderer.block.BlockRenderData;
import game.client.renderer.block.IBlockRenderer;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.model.StreamModel;
import game.client.renderer.tessellation.Tessellator;
import game.common.block.Block;
import game.common.util.BlockPos;
import game.common.world.cluster.Cluster;

public class ClusterRenderData {

	private StreamModel model;
	private StreamModel transparentModel;

	private ClusterRenderData(StreamModel model){
		this.model = model;
	}

	public StreamModel getModel(){
		return model;
	}
	
	public StreamModel getTransparentModel(){
		return transparentModel;
	}

	public void onUnloaded(){
		model.delete();
		transparentModel.delete();
	}

	public static ClusterRenderData createFromCluster(Cluster cluster){
		Tessellator tessellator = new Tessellator(Tessellator.MODE_TRIANGLED_QUAD);
		Tessellator transparentTessellator = new Tessellator(Tessellator.MODE_TRIANGLED_QUAD);

		for(int i = 0; i < Cluster.CLUSTER_SIZE; i++){
			for(int j = 0; j < Cluster.CLUSTER_SIZE; j++){
				for(int k = 0; k < Cluster.CLUSTER_SIZE; k++){

					Block block = cluster.getBlock(i, j, k);
					BlockPos blockPos = cluster.getPosition().getWorldCoords(i, j, k);

					if(block.shouldBlockBeRendered(cluster.getWorld(), blockPos)){
						BlockRenderData brd = RenderRegistry.getBlockRenderData(block);
						if(brd == null){
							System.out.println("No block render data found for block " + block.getUnlocalizedName() + "!");
							continue;
						}
						IBlockRenderer renderer = RenderRegistry.getBlockRenderer(brd.getRenderer());
						if(renderer != null){
							if(!block.isTransparantBlock()){
								tessellator.setTessellateOffset(i, j, k);
								renderer.tessellateBlock(tessellator, cluster.getWorld(), brd, block, blockPos);
							}
							else{
								transparentTessellator.setTessellateOffset(i, j, k);
								renderer.tessellateBlock(transparentTessellator, cluster.getWorld(), brd, block, blockPos);
							}
						}
						else{
							System.out.println("No block renderer found for block " + block.getUnlocalizedName() + "!");
						}
					}
				}
			}
		}

		StreamModel model = ModelLoader.INSTANCE.createDynamicModel();

		model.bindModel();
		model.createIndexBuffer(tessellator.getIndexBuffer());
		model.createDataBuffer("positions", 0, 3, tessellator.getVertexBuffer());
		model.createDataBuffer("uvs", 1, 2, tessellator.getUvBuffer());
		model.createDataBuffer("normals", 2, 3, tessellator.getNormalBuffer());
		model.createDataBuffer("tangents", 3, 3, tessellator.getTangentBuffer());
		model.unbindModel();

		StreamModel transparentModel = ModelLoader.INSTANCE.createDynamicModel();
		
		transparentModel.bindModel();
		transparentModel.createIndexBuffer(transparentTessellator.getIndexBuffer());
		transparentModel.createDataBuffer("positions", 0, 3, transparentTessellator.getVertexBuffer());
		transparentModel.createDataBuffer("uvs", 1, 2, transparentTessellator.getUvBuffer());
		transparentModel.createDataBuffer("normals", 2, 3, transparentTessellator.getNormalBuffer());
		transparentModel.createDataBuffer("tangents", 3, 3, transparentTessellator.getTangentBuffer());
		transparentModel.unbindModel();
		
		ClusterRenderData crd = new ClusterRenderData(model);
		crd.transparentModel = transparentModel;

		return crd;
	}
}
