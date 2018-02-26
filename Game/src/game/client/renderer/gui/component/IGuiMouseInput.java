package game.client.renderer.gui.component;

public interface IGuiMouseInput {

	void onMouseMoved(int mouseX, int mouseY);
	void onMouseClicked(int mouseX, int mouseY, int button, int action);
}
