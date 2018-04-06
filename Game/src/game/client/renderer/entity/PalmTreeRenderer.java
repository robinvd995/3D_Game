package game.client.renderer.entity;

import caesar.util.Vector3f;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;
import game.common.entity.Entity;

public class PalmTreeRenderer extends EntityRenderer{

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("entity/palm_tree", 256, 256);
	
	public PalmTreeRenderer() {
		super("palm_tree");
	}

	@Override
	public void renderEntity(Shader shader, Entity entity, double delta) {
		shader.loadVector3f("blockColor", new Vector3f(1.0f, 1.0f, 1.0f));
		TextureManager.bindTexture(TEXTURE);
		model.renderAll();
	}

}
