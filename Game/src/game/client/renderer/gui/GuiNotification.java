package game.client.renderer.gui;

import caesar.util.Vector4f;
import game.client.renderer.gui.component.GuiComponentBackground;
import game.client.renderer.gui.component.GuiComponentDynamicText;
import game.client.renderer.gui.component.IGuiComponent;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiNotification extends Gui {

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/notification", 512, 512);

	private GuiComponentDynamicText text;
	
	private String message;
	
	public GuiNotification() {
		super(0, 0, 400, 200);
	}

	@Override
	public void initGui() {
		this.guiPosX = (screenWidth - guiWidth) / 2;
		this.guiPosY = (screenHeight - guiHeight) / 2;

		this.addComponent(new GuiComponentBackground(this, TEXTURE));
		this.addComponent(this.text = new GuiComponentDynamicText(this, "Connecting...", FontManager.getFont("arial"), guiWidth / 2, 80, GuiAnchor.TOP, new Vector4f(1.0f), 0.5f));
	}

	@Override
	public void actionPerformed(IGuiComponent component, int action){
		
	}
	
	public void renderGui(Shader shader, int renderPass){
		if(renderPass == 0) text.updateText(message);
		super.renderGui(shader, renderPass);
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
	
	public void setMessage(String message){
		this.message = message;
	}

}
