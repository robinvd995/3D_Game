package game.renderer.gui;

import caesar.util.Vector4f;
import game.renderer.gui.component.GuiComponentBackground;
import game.renderer.gui.component.GuiComponentText;
import game.renderer.gui.font.FontManager;
import game.renderer.texture.TextureData;
import game.renderer.texture.TextureManager;

public class GuiInventory extends Gui {

	private static final TextureData TEXTURE = TextureManager.createDefaultGuiTexture("gui/inventory");
	
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
		return 1;
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
