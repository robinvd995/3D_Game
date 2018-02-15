package game.renderer.gui.component;

import caesar.util.Vector2f;
import game.renderer.gui.Gui;
import game.renderer.gui.Gui.GuiQuad;

public class GuiComponentCrosshair implements IGuiComponent{

	private static final String TEXTURE = "gui/crosshair2";

	private GuiQuad quad;

	@Override
	public void onComponentAdded(Gui gui) {
		quad = gui.createQuad(-32, -32, 64, 64, 0, 0, 64, 64);
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
		return new Vector2f(0, 0);
	}

	@Override
	public void onComponentDeleted(Gui gui) {
		quad.deleteQuad();
	}
}
