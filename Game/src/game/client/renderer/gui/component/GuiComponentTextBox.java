package game.client.renderer.gui.component;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import game.client.renderer.gui.Gui;
import game.client.renderer.gui.GuiAnchor;
import game.client.renderer.gui.font.Font;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureManager;

public class GuiComponentTextBox implements IGuiRenderComponent, IGuiTextComponent, IGuiMouseInput, IGuiCharInput, IGuiKeyInput{

	private static final TextureData TEXTURE = TextureManager.getDefaultGuiTexture("gui/buttons", 512, 512);
	
	private Gui gui;

	private int posX;
	private int posY;

	private int textBoxWidth = 256;
	private int textBoxHeight = 48;

	private String text = "";
	private Font font;

	private StreamModel quadModel;
	private StreamModel textModel;

	private boolean isSelected = false;

	public GuiComponentTextBox(Gui gui, int posX, int posY, String font){
		this.gui = gui;
		this.posX = posX;
		this.posY = posY;
		this.font = FontManager.getFont(font);
	}

	@Override
	public void onComponentAdded() {
		quadModel = gui.createDynamicQuad(GuiAnchor.TOP_LEFT, textBoxWidth, textBoxHeight, 256, 0,TEXTURE);
		textModel = gui.createDynamicTextMesh(font, text, GuiAnchor.TOP_LEFT, 0.5f);
	}

	@Override
	public void onComponentDeleted() {
		quadModel.delete();
		textModel.delete();

	}

	@Override
	public void onCharTyped(char character) {
		if(isSelected){
			text = text + character;
			gui.updateText(textModel, font, text, GuiAnchor.TOP_LEFT, 0.5f);
		}
	}
	
	@Override
	public void onKeyAction(int key, int action, int scancode, int mods) {
		if(isSelected && key == GLFW.GLFW_KEY_BACKSPACE && action != 0){
			int newLength = text.length() - 1;
			if(newLength >= 0){
				text = text.substring(0, newLength);
				gui.updateText(textModel, font, text, GuiAnchor.TOP_LEFT, 0.5f);
			}
		}
	}

	@Override
	public void onMouseMoved(int mouseX, int mouseY) {}

	@Override
	public void onMouseClicked(int mouseX, int mouseY, int button, int action) {
		boolean inRange = mouseX > posX && mouseX < posX + textBoxWidth && mouseY > posY && mouseY < posY + textBoxHeight;
		if(button == 0 && action == 1){
			isSelected = inRange;
			int index = isSelected ? 1 : 0;
			gui.updateQuadTexture(quadModel, textBoxWidth, textBoxHeight, 256, index * textBoxHeight, TEXTURE.getWidth(), TEXTURE.getHeight());
		}
	}

	@Override
	public void renderText(Shader shader) {
		textModel.bindModel();
		shader.enableAttribArrays();
		GL11.glDrawElements(GL11.GL_TRIANGLES, textModel.getSize(), GL11.GL_UNSIGNED_INT, 0);
		shader.disableAttribArrays();
		textModel.unbindModel();
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public int getTextPosX() {
		return posX + 10;
	}

	@Override
	public int getTextPosY() {
		return posY + 10;
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

	public String getText(){
		return text;
	}
}
