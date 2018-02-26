package game.client.renderer.entity;

import caesar.util.Vector3f;
import game.client.renderer.shader.EntityShader;
import game.common.entity.Entity;
import game.common.entity.Player;

public class PlayerRenderer extends EntityRenderer{

	public PlayerRenderer() {
		super("player");
	}

	@Override
	public void renderEntity(EntityShader shader, Entity entity) {
		Player player = (Player) entity;
		shader.loadBlockColor(new Vector3f(1.0f, 0.0f, 0.0f));
		model.renderAll();
	}

}
