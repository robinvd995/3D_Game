package game.renderer.gui.component;

import game.renderer.shader.Shader;
import game.renderer.texture.TextureData;

public interface IGuiRenderComponent extends IGuiComponent{

	void renderComponent(Shader shader);
	TextureData getTexture();
	int getRenderPosX();
	int getRenderPosY();
}
