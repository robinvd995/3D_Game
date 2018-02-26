package game.client.renderer.gui.component;

import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;

public interface IGuiRenderComponent extends IGuiComponent{

	void renderComponent(Shader shader);
	TextureData getTexture();
	int getRenderPosX();
	int getRenderPosY();
}
