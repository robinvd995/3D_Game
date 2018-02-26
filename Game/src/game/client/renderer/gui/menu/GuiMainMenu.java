package game.client.renderer.gui.menu;

import game.client.display.DisplayManager;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.component.GuiComponentButton;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.component.IGuiComponent;
import game.client.renderer.gui.event.GuiEvent.GuiCloseEvent;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.scene.SceneGame;
import game.common.event.EventManager;
import game.common.event.SetSceneEvent;

public class GuiMainMenu extends Gui{

	public GuiMainMenu() {
		super(0, 0);
	}

	@Override
	protected void initGui() {
		this.guiWidth = this.screenWidth;
		this.guiHeight = this.screenHeight;
		
		int y = screenHeight / 2 - 200;
		
		this.addComponent(new GuiComponentText(this, "3D Game", "arial", screenWidth / 2, y, GuiAnchor.TOP));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 100, 0, "Start Game", "arial"));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 164, 1, "Mulitplayer", "arial"));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 228, 2, "Options", "arial"));
		this.addComponent(new GuiComponentButton(this, screenWidth / 2 - 128, y + 292, 3, "Quit", "arial"));
	}
	
	@Override
	public void actionPerformed(IGuiComponent component, int action){
		switch(action){
		case 0:
			EventManager.postPostUpdateEvent(new SetSceneEvent(new SceneGame()));
			break;
		case 1:
			EventManager.postPostUpdateEvent(new GuiOpenEvent(new GuiMenuMultiplayer()));
			EventManager.postPostUpdateEvent(new GuiCloseEvent(getGuiId()));
			break;
		case 2: break;
		case 3:
			DisplayManager.INSTANCE.invokeCloseWindow();
			break;
		}
	}

	@Override
	public int getGuiLayer() {
		return 0;
	}

	@Override
	public String getGuiId() {
		return "main_menu";
	}

}
