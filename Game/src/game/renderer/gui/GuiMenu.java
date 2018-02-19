package game.renderer.gui;

import game.input.IKeyListener;
import game.input.IMouseListener;
import game.renderer.gui.component.GuiComponentBackground;
import game.renderer.gui.component.GuiComponentButton;
import game.renderer.gui.component.GuiComponentDynamicText;
import game.renderer.gui.component.GuiComponentText;
import game.renderer.gui.font.FontManager;
import game.renderer.texture.TextureData;
import game.renderer.texture.TextureManager;

public class GuiMenu extends Gui implements IKeyListener, IMouseListener{

	private static final TextureData TEXTURE = TextureManager.createDefaultGuiTexture("gui/background_menu");
	
	public GuiMenu() {
		super(0, 0, 400, 600);
	}

	@Override
	public void initGui() {
		this.guiPosX = (screenWidth - guiWidth) / 2;
		this.guiPosY = (screenHeight - guiHeight) / 2;
		
		this.addComponent(new GuiComponentBackground(this, TEXTURE));
		this.addComponent(new GuiComponentText(this, "Menu", FontManager.getFont("arial"), guiWidth / 2, 10, GuiAnchor.TOP));
		this.addComponent(new GuiComponentButton(this, 72, 100, 0, "Button 1", "arial"));
		this.addComponent(new GuiComponentButton(this, 72, 156, 1, "Button 2", "arial"));
		this.addComponent(new GuiComponentButton(this, 72, 212, 2, "Button 3", "arial"));
		this.addComponent(new GuiComponentButton(this, 72, 268, 3, "Button 4", "arial"));
		this.addComponent(new GuiComponentButton(this, 72, 324, 4, "Button 5", "arial"));
	}
	
	@Override
	public void actionPerformed(int action){
		
	}

	@Override
	public int getGuiLayer() {
		return 2;
	}

	@Override
	public String getGuiId() {
		return "menu";
	}
}
