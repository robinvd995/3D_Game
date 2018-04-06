package game.client.renderer.item;

import java.io.File;

import caesar.util.GlobalAxis;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import game.client.renderer.gui.Gui;
import game.client.renderer.model.LoadedModel;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;
import game.common.entity.Entity;
import game.common.entity.Player;
import game.common.util.MathHelper;

public class ItemRendererPickaxe implements IItemRenderer{

	protected static final String MODEL_FOLDER = "res/models/items/";
	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("items/pickaxe", 256, 256);

	private LoadedModel model;

	public ItemRendererPickaxe(){
		IndexedModel indexedModel = OIMModelLoader.loadModel(new File(MODEL_FOLDER + "pickaxe" + ".oim"));
		if(indexedModel != null){
			model = ModelLoader.INSTANCE.loadIndexedModel(indexedModel);
		}
	}

	@Override
	public void renderItemInHand(Shader shader, Entity entity, double delta) {
		Player player = (Player) entity;
		/*shader.loadVector3f("blockColor", new Vector3f(1.0f, 1.0f, 1.0f));
		RenderManager.waterRenderer.getBuffer().bindDepthTexture();
		model.renderAll();*/
		
		Vector3f position = player.getCamera().getTransform().getPosition().copy();
		Quaternion orientation = player.getCamera().getTransform().getOrientation().inverse();
		
		Matrix4f modelMatrix = MathHelper.createTransformationMatrix(position, orientation);
		Vector3f HAND_TRANSLATION = new Vector3f(0.35f,-0.6f,-0.5f);
		modelMatrix.translate(HAND_TRANSLATION, modelMatrix);
		
		player.getAnimator().animate(delta, modelMatrix);
		
		Matrix4f.rotate((float)(Math.PI * 0.5f), GlobalAxis.Y.toVector(), modelMatrix, modelMatrix);
		modelMatrix.scale(new Vector3f(0.05f));
		//Matrix4f.translate(position, modelMatrix, modelMatrix);
		//Matrix4f.scale(new Vector3f(0.2f), modelMatrix, modelMatrix);
		
		shader.loadMatrix("modelMatrix", modelMatrix);
		shader.loadVector3f("blockColor", new Vector3f(1.0f, 1.0f, 1.0f));
		TextureManager.bindTexture(TEXTURE);
		model.renderAll();
	}

	@Override
	public void renderItemInGui(Shader shader, Gui gui) {
		throw new RuntimeException("Not yet implemented!");
	}

	@Override
	public void renderItemOnGround(Shader shader, Entity entity) {
		throw new RuntimeException("Not yet implemented!");
	}

	@Override
	public LoadedModel getModel(Entity entity) {
		return model;
	}

}
