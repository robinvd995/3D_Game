package game.client.renderer.gui;

import caesar.util.Vector4f;
import game.client.renderer.gui.component.GuiComponentBackground;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiInventory extends Gui {

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/inventory", 1024, 1024);
	
	private static int lastInventoryPositionX = 200;
	private static int lastInventoryPositionY = 200;
	
	public GuiInventory() {
		super(lastInventoryPositionX, lastInventoryPositionY, 400, 400);
	}

	@Override
	protected void initGui() {
		this.addComponent(new GuiComponentBackground(this, TEXTURE));
		this.addComponent(new GuiComponentText(this, "Inventory", FontManager.getFont("arial"), guiWidth / 2, 20, GuiAnchor.CENTER, new Vector4f(1.0f), 0.5f));
	}

	@Override
	public int getGuiLayer() {
		return this.isActive() ? 2 : 1;
	}

	@Override
	public String getGuiId() {
		return "inventory";
	}

	public boolean doesGuiDisablePlayerMovement(){
		return false;
	}
	
	public boolean canDrag(){
		return true;
	}

	public void dispose(){
		super.dispose();
		lastInventoryPositionX = guiPosX;
		lastInventoryPositionY = guiPosY;
	}
}
