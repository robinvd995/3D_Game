package game.client.renderer.gui;

import caesar.util.Vector4f;
import game.client.Game;
import game.client.renderer.gui.component.GuiComponentCrosshair;
import game.client.renderer.gui.component.GuiComponentDynamicText;
import game.client.renderer.gui.component.GuiComponentHudTexture;
import game.client.renderer.gui.component.GuiComponentText;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.shader.Shader;

public class GuiHud extends Gui {
	
	private GuiComponentDynamicText fpsCounter;
	
	public GuiHud(int x, int y) {
		super(x, y);
	}

	@Override
	public void initGui() {
		this.guiWidth = screenWidth;
		this.guiHeight = screenHeight;
		int screenCenterX = screenWidth / 2;
		int screenCenterY = screenHeight / 2;
		this.addComponent(new GuiComponentCrosshair(this, screenCenterX, screenCenterY));
		int slotAmount = 10;
		int offset = slotAmount / 2;
		this.addComponent(new GuiComponentText(this, "Fps:", FontManager.getFont("arial"), 10 , 10, GuiAnchor.TOP_LEFT, new Vector4f(1.0f), 0.33f));
		this.fpsCounter = new GuiComponentDynamicText(this, "", FontManager.getFont("arial"), 50, 10, GuiAnchor.TOP_LEFT, new Vector4f(1.0f), 0.33f);
		this.addComponent(fpsCounter);
		for(int i = 0; i < slotAmount; i++){
			int posX = screenCenterX + ((i - offset) * 66);
			this.addComponent(new GuiComponentHudTexture(this, posX, screenHeight - 66, 0, 0, 64, 0, 64, 64));
		}
	}
	
	@Override
	public void renderGui(Shader shader, int renderPass){
		fpsCounter.updateText(String.valueOf(Game.INSTANCE.timer.getFps()));
		super.renderGui(shader, renderPass);
	}
	
	@Override
	public int getGuiLayer() {
		return 0;
	}

	@Override
	public String getGuiId() {
		return "hud";
	}

	public boolean doesGuiDisablePlayerMouse(){
		return false;
	}
	
	public boolean doesGuiDisablePlayerMovement(){
		return false;
	}
	
	public boolean canBeActive(){
		return false;
	}
}
