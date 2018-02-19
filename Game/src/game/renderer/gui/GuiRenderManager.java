package game.renderer.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import caesar.util.Vector2f;
import caesar.util.Vector4f;
import game.Game;
import game.display.DisplayManager;
import game.display.IDisplaySizeListener;
import game.renderer.gui.font.FontManager;
import game.renderer.model.SimpleModel;
import game.renderer.shader.Shader;
import game.renderer.shader.ShaderBuilder;

public class GuiRenderManager implements IDisplaySizeListener{

	private List<Gui> guisToRender = new ArrayList<Gui>();

	private Shader shader;

	public GuiRenderManager(){
		guisToRender = new ArrayList<Gui>();
	}

	public void initRenderer() {
		shader = ShaderBuilder.buildShader("gui");
		DisplayManager.INSTANCE.addDisplaySizeListener(this);
		FontManager.loadFont("arial");
	}

	public void renderGuis(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
		shader.loadVector4f("color", new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		for(int i = 0; i < 2; i++){
			for(Gui gui : guisToRender){
				gui.renderGui(shader, i);
			}
		}
		
		Vector2f elementPosition = new Vector2f();
		int screenWidth = DisplayManager.INSTANCE.getDisplayWidth();
		int screenHeight = DisplayManager.INSTANCE.getDisplayHeight();
		for(Gui gui : guisToRender){
			SimpleModel quad = gui.getGuiQuad();
			quad.bindModel();
			int texture = gui.getGuiColorTexture();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			shader.enableAttribArrays();
			float posX = (float)((gui.getGuiPosX()) * 2.0f) / (float)screenWidth - 1.0f;
			float posY = -((float)((gui.getGuiPosY()) * 2.0f) / (float)screenHeight - 1.0f);
			elementPosition.set(posX, posY);
			shader.loadVector2f("elementPosition", elementPosition);
			GL11.glDrawElements(GL11.GL_TRIANGLES, quad.getSize(), GL11.GL_UNSIGNED_INT, 0);
			shader.disableAttribArrays();
			quad.unbindModel();
		}
		
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}

	public Gui getGui(String guiId){
		for(Gui gui : guisToRender){
			if(gui.getGuiId().equals(guiId)){
				return gui;
			}
		}
		return null;
	}
	
	public void addGui(Gui gui){
		gui.init(DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight());
		guisToRender.add(gui);
		sortGuis();
		checkForActiveGui();
	}

	public void closeGui(Gui gui){
		gui.dispose();
		guisToRender.remove(gui);
		checkForActiveGui();
	}

	private void sortGuis(){
		guisToRender.sort(new Comparator<Gui>(){

			@Override
			public int compare(Gui gui0, Gui gui1) {
				return gui0.getGuiLayer() - gui1.getGuiLayer();
			}

		});
	}

	public boolean isGuiOpen(Gui gui){
		return guisToRender.contains(gui);
	}

	public void checkForActiveGui(){
		boolean disableMouse = false;
		boolean disableMovement = false;
		for(Gui gui : guisToRender){
			if(gui.doesGuiDisablePlayerMouse()){
				disableMouse = true;
			}
			if(gui.doesGuiDisablePlayerMovement()){
				disableMovement = true;
			}
		}
		Game.INSTANCE.setMouseDisabled(disableMouse);
		Game.INSTANCE.setMovementDisabled(disableMovement);
	}

	/*private void checkRequiredRenderPasses() {
		for(Gui gui : guisToRender){
			if(gui.getRequiredRenderPasses() > requiredRenderPasses){
				requiredRenderPasses = gui.getRequiredRenderPasses();
			}
		}
	}*/

	@Override
	public void onDisplaySizeChanged(DisplayManager displayManager) {
		for(Gui gui : guisToRender){
			gui.dispose();
			gui.init(displayManager.getDisplayWidth(), displayManager.getDisplayHeight());
		}
	}
}
