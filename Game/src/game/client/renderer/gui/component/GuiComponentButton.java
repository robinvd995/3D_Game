package game.client.renderer.gui.component;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector4f;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.font.Font;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiComponentButton implements IGuiRenderComponent, IGuiTextComponent, IGuiMouseInput{

	public static final int BUTTON_STATE_DISABLED = 0;
	public static final int BUTTON_STATE_ACTIVE = 1;
	public static final int BUTTON_STATE_HOVER = 2;
	public static final int BUTTON_STATE_PRESSED = 3;
	
	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/buttons", 512, 512);
	
	private final Gui gui;
	
	private final int posX;
	private final int posY;
	private final int action;
	
	private int buttonWidth = 256;
	private int buttonHeight = 48;
	
	private String text;
	private Font font;
	
	private StreamModel quadModel;
	private SimpleModel textModel;
	
	private Vector4f fontColor = new Vector4f(1.0f);
	
	private int buttonState = BUTTON_STATE_ACTIVE;
	
	public GuiComponentButton(Gui gui, int x, int y, int action, String text, String font){
		this.gui = gui;
		this.posX = x;
		this.posY = y;
		this.action = action;
		this.text = text;
		this.font = FontManager.getFont(font);
	}
	
	@Override
	public void onComponentAdded() {
		quadModel = gui.createDynamicQuad(GuiAnchor.TOP_LEFT, buttonWidth, buttonHeight, 0, buttonState * buttonHeight, TEXTURE);
		textModel = gui.createTextMesh(font, text, GuiAnchor.CENTER, 0.5f);
	}

	@Override
	public void onComponentDeleted() {
		quadModel.delete();
		textModel.delete();
	}

	@Override
	public void renderText(Shader shader) {
		textModel.bindModel();
		shader.enableAttribArrays();
		shader.loadVector4f("color", fontColor);
		//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, textModel.getSize());
		GL11.glDrawElements(GL11.GL_TRIANGLES, textModel.getSize(), GL11.GL_UNSIGNED_INT, 0);
		shader.loadVector4f("color", new Vector4f(1.0f));
		shader.disableAttribArrays();
		textModel.unbindModel();
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public int getTextPosX() {
		return posX + buttonWidth / 2;
	}

	@Override
	public int getTextPosY() {
		return posY + buttonHeight / 2;
	}

	@Override
	public void renderComponent(Shader shader) {
		quadModel.bindModel();
		shader.enableAttribArrays();
		GL11.glDrawElements(GL11.GL_TRIANGLES, quadModel.getSize(), GL11.GL_UNSIGNED_INT, 0);
		shader.disableAttribArrays();
		quadModel.unbindModel();
	}

	@Override
	public TextureData getTexture() {
		return TEXTURE;
	}

	@Override
	public int getRenderPosX() {
		return posX;
	}

	@Override
	public int getRenderPosY() {
		return posY;
	}

	@Override
	public void onMouseMoved(int mouseX, int mouseY) {
		if(buttonState == BUTTON_STATE_ACTIVE && mouseX > posX && mouseX < posX + buttonWidth && mouseY > posY && mouseY < posY + buttonHeight){
			setButtonState(BUTTON_STATE_HOVER);
		}
		
		else if(buttonState == BUTTON_STATE_HOVER && !(mouseX > posX && mouseX < posX + buttonWidth && mouseY > posY && mouseY < posY + buttonHeight)){
			setButtonState(BUTTON_STATE_ACTIVE);
		}
	}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int button, int action) {
		if(buttonState == BUTTON_STATE_HOVER && button == 0 && action == 1){
			setButtonState(BUTTON_STATE_PRESSED);
		}
		
		if(buttonState == BUTTON_STATE_PRESSED && button == 0 && action == 0){
			boolean inRange = mouseX > posX && mouseX < posX + buttonWidth && mouseY > posY && mouseY < posY + buttonHeight;
			if(inRange){
				setButtonState(BUTTON_STATE_HOVER);
				gui.actionPerformed(this, this.action);
			}
			else{
				setButtonState(BUTTON_STATE_ACTIVE);
			}
		}
	}

	public void setButtonState(int state){
		if(buttonState != state){
			buttonState = state;
			gui.updateQuadTexture(quadModel, buttonWidth, buttonHeight, 0, buttonState * buttonHeight, TEXTURE.getWidth(), TEXTURE.getHeight());
		}
	}
}
