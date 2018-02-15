package game.renderer.gui.component;

import caesar.util.Vector2f;
import game.renderer.gui.Gui;

public interface IGuiComponent {
	
	void onComponentAdded(Gui gui);
	void renderComponent(Gui gui);
	Vector2f getPosition();
	void onComponentDeleted(Gui gui);
}
