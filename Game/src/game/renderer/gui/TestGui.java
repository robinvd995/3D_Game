package game.renderer.gui;

import game.renderer.gui.component.GuiComponentCrosshair;

public class TestGui extends Gui{

	public TestGui() {
		super();
	}

	@Override
	public void initGui() {
		//this.addComponent(new GuiComponentBackground());
		this.addComponent(new GuiComponentCrosshair());
	}

}
