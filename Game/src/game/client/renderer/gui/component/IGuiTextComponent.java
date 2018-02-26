package game.client.renderer.gui.component;

import game.client.renderer.gui.font.Font;
import game.client.renderer.shader.Shader;

public interface IGuiTextComponent extends IGuiComponent{

	void renderText(Shader shader);
	Font getFont();
	int getTextPosX();
	int getTextPosY();
}
