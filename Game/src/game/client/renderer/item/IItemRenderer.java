package game.client.renderer.item;

import game.client.renderer.gui.Gui;
import game.client.renderer.model.LoadedModel;
import game.client.renderer.shader.Shader;
import game.common.entity.Entity;

public interface IItemRenderer {

	void renderItemInHand(Shader shader, Entity entity, double delta);
	void renderItemInGui(Shader shader, Gui gui);
	void renderItemOnGround(Shader shader, Entity entity);
	LoadedModel getModel(Entity entity);
}
