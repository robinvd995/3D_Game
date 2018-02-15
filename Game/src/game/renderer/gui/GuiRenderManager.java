package game.renderer.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import game.display.DisplayManager;
import game.display.IDisplaySizeListener;
import game.renderer.shader.GuiShader;

public class GuiRenderManager implements IDisplaySizeListener{

	private List<Gui> guisToRender = new ArrayList<Gui>();
	
	private GuiShader shader;
	
	public GuiRenderManager(){
		guisToRender = new ArrayList<Gui>();
	}
	
	public void initRenderer() {
		shader = new GuiShader();
		DisplayManager.INSTANCE.addDisplaySizeListener(this);
	}
	
	public void renderGuis(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		shader.start();
		for(Gui gui : guisToRender){
			gui.renderGui(shader);
		}
		shader.stop();
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void addGui(Gui gui){
		guisToRender.add(gui);
	}

	@Override
	public void onDisplaySizeChanged(DisplayManager displayManager) {
		for(Gui gui : guisToRender){
			gui.dispose();
			gui.init(displayManager.getDisplayWidth(), displayManager.getDisplayHeight());
		}
	}
}
