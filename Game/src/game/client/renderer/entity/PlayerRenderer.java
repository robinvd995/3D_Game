package game.client.renderer.entity;

import game.client.renderer.RenderRegistry;
import game.client.renderer.item.IItemRenderer;
import game.client.renderer.shader.Shader;
import game.client.scene.SceneGame;
import game.common.entity.Entity;
import game.common.entity.Player;
import game.common.item.Item;

public class PlayerRenderer extends EntityRenderer{
	
	public PlayerRenderer() {
		//super("sword");
	}

	@Override
	public void renderEntity(Shader shader, Entity entity, double delta) {
		Player player = (Player) entity;
		Item item = player.getCurrentHeldItem();
		if(item != null){	
			IItemRenderer renderer = RenderRegistry.getItemRenderer(item);
			renderer.renderItemInHand(shader, player, delta);
		}
	}

	@Override
	public void bindModel(){
		Item item = SceneGame.getPlayer().getCurrentHeldItem();
		if(item != null){
			IItemRenderer renderer = RenderRegistry.getItemRenderer(item);
			renderer.getModel(SceneGame.getPlayer()).bindModel();
		}
	}
}
