package game.client.renderer.gui;

import game.client.renderer.gui.component.GuiComponentBackground;
import game.client.renderer.gui.component.GuiComponentButton;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.component.IGuiComponent;
import game.client.renderer.gui.event.GuiEvent.GuiCloseEvent;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;
import game.client.scene.SceneMenu;
import game.common.event.EventManager;
import game.common.event.SetSceneEvent;

public class GuiMenu extends Gui {

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/background_menu", 1024, 1024);
	
	public GuiMenu() {
		super(0, 0, 400, 600);
	}

	@Override
	public void initGui() {
		this.guiPosX = (screenWidth - guiWidth) / 2;
		this.guiPosY = (screenHeight - guiHeight) / 2;
		
		this.addComponent(new GuiComponentBackground(this, TEXTURE));
		this.addComponent(new GuiComponentText(this, "Menu", FontManager.getFont("arial"), guiWidth / 2, 10, GuiAnchor.TOP));
		this.addComponent(new GuiComponentButton(this, 72, 100, 0, "Continue", "arial"));
		this.addComponent(new GuiComponentButton(this, 72, 156, 1, "Quit", "arial"));
	}
	
	@Override
	public void actionPerformed(IGuiComponent component, int action){
		switch(action){
		case 0:
			EventManager.postPostUpdateEvent(new GuiCloseEvent(this.getGuiId()));
			break;
		case 1:
			EventManager.postPostUpdateEvent(new SetSceneEvent(new SceneMenu()));
			break;
		}
	}

	@Override
	public int getGuiLayer() {
		return 3;
	}

	@Override
	public String getGuiId() {
		return "menu";
	}
	
	public boolean isAlwaysActive(){
		return true;
	}
}
