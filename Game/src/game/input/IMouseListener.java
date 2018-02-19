package game.input;

public interface IMouseListener {

	void onMouseMoved(int mouseX, int mouseY);
	void onMouseClicked(int button, int action, int mods);
}
