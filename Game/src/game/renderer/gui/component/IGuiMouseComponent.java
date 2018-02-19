package game.renderer.gui.component;

import game.renderer.gui.Gui;

public interface IGuiMouseComponent extends IGuiComponent{

	void onMouseMoved(int mouseX, int mouseY);
	void onMouseClicked(int mouseX, int mouseY, int button, int action);
}
