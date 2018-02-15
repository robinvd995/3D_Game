package game.renderer.gui.component;

import caesar.util.Vector2f;
import game.renderer.gui.Gui;
import game.renderer.gui.Gui.GuiQuad;

public class GuiComponentBackground implements IGuiComponent{

	private static final String TEXTURE = "gui/background";
	
	private GuiQuad quad;
	
	@Override
	public void onComponentAdded(Gui gui) {
		quad = gui.createQuad(-100, -100, 256, 256, 0, 0, 256, 256);
	}

	@Override
	public void renderComponent(Gui gui) {
		gui.bindTexture(TEXTURE);
		quad.bindQuad();
		quad.renderQuad();
		quad.unbindQuad();
	}

	@Override
	public Vector2f getPosition() {
		return new Vector2f(0.0f, 0.0f);
	}

	@Override
	public void onComponentDeleted(Gui gui) {
		quad.deleteQuad();
	}
}
