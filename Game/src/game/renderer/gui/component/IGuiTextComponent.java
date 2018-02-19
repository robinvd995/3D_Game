package game.renderer.gui.component;

import game.renderer.gui.Gui;
import game.renderer.gui.font.Font;
import game.renderer.shader.Shader;

public interface IGuiTextComponent extends IGuiComponent{

	void renderText(Shader shader);
	Font getFont();
	int getTextPosX();
	int getTextPosY();
}
