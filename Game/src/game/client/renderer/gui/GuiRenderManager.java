package game.client.renderer.gui;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.eventbus.Subscribe;

import caesar.util.Vector2f;
import caesar.util.Vector4f;
import game.client.Game;
import game.client.display.DisplayManager;
import game.client.display.IDisplaySizeListener;
import game.client.input.ICharListener;
import game.client.input.IKeyListener;
import game.client.input.IMouseListener;
import game.client.input.InputManager;
import game.client.renderer.gui.event.GuiEvent.GuiCloseEvent;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.gui.font.FontManager;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;
import game.common.event.EventManager;

public class GuiRenderManager implements IDisplaySizeListener, IKeyListener, IMouseListener, ICharListener{

	private List<Gui> guisToRender;

	private Gui activeGui;

	private Shader shader;

	private int mouseX;
	private int mouseY;

	public GuiRenderManager(){
		guisToRender = new LinkedList<Gui>();
	}

	public void initRenderer() {
		shader = ShaderBuilder.buildShader("gui");
		DisplayManager.INSTANCE.addDisplaySizeListener(this);
		FontManager.loadFont("arial");

		EventManager.registerEventListener(this);
		InputManager.addKeyListener(this);
		InputManager.addCharListener(this);
		InputManager.addMouseListener(this);
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

	private void sortGuis(){
		guisToRender.sort(new Comparator<Gui>(){

			@Override
			public int compare(Gui gui0, Gui gui1) {

				int layer0 = gui0.getGuiLayer();
				int layer1 = gui1.getGuiLayer();

				return layer0 - layer1;
			}

		});
	}

	public boolean isGuiOpen(Gui gui){
		return guisToRender.contains(gui);
	}

	public void checkForActiveGui(){
		boolean disableMouse = false;
		boolean disableMovement = false;
		/*for(Gui gui : guisToRender){
			if(gui.doesGuiDisablePlayerMouse()){
				disableMouse = true;
			}
			if(gui.doesGuiDisablePlayerMovement()){
				disableMovement = true;
			}
		}*/
		if(activeGui != null && activeGui.doesGuiDisablePlayerMouse()){
			disableMouse = true;
		}
		if(activeGui != null && activeGui.doesGuiDisablePlayerMovement()){
			disableMovement = true;
		}
		Game.INSTANCE.setMouseDisabled(disableMouse);
		Game.INSTANCE.setMovementDisabled(disableMovement);
	}

	@Override
	public void onDisplaySizeChanged(DisplayManager displayManager) {
		for(Gui gui : guisToRender){
			gui.dispose();
			gui.init(displayManager.getDisplayWidth(), displayManager.getDisplayHeight());
		}
	}

	public void closeAllGuis(){
		for(Gui gui : guisToRender){
			gui.dispose();
		}
		activeGui = null;
		guisToRender.clear();
	}

	@Subscribe
	public void onGuiOpenEvent(GuiOpenEvent event){
		if(!isGuiOpen(event.getGui())){
			openGui(event.getGui());
		}else{
			String guiid = event.getGui().getGuiId();
			/*if(activeGui != null && !activeGui.getGuiId().equals(guiid)){
				setActiveGui(event.getGui());
			}
			else if(active)*/
			if(activeGui != null && activeGui.getGuiId().equals(guiid)){
				closeGui(getGui(guiid));
			}
			else{
				setActiveGui(getGui(guiid));
				checkForActiveGui();
			}
		}
	}

	@Subscribe
	public void onGuiCloseEvent(GuiCloseEvent event){
		Gui gui = getGui(event.getGuiId());
		if(gui != null){
			closeGui(gui);
		}
	}

	private void openGui(Gui gui){
		gui.init(DisplayManager.INSTANCE.getDisplayWidth(), DisplayManager.INSTANCE.getDisplayHeight());
		guisToRender.add(gui);

		setActiveGui(gui);

		sortGuis();
		checkForActiveGui();

		gui.onGuiOpened();
		
		if(gui instanceof IMouseListener){
			InputManager.addMouseListener((IMouseListener) gui);
		}
		if(gui instanceof IKeyListener){
			InputManager.addKeyListener((IKeyListener) gui);
		}
		if(gui instanceof ICharListener){
			InputManager.addCharListener((ICharListener) gui);
		}
	}

	private void closeGui(Gui gui){
		gui.dispose();
		guisToRender.remove(gui);
		activeGui = getNextInactiveGui();
		checkForActiveGui();
		
		gui.onGuiClosed();
		
		if(gui instanceof IMouseListener){
			InputManager.removeMouseListener((IMouseListener) gui);
		}
		if(gui instanceof IKeyListener){
			InputManager.removeKeyListener((IKeyListener) gui);
		}
		if(gui instanceof ICharListener){
			InputManager.removeCharListener((ICharListener) gui);
		}
	}

	private Gui getNextInactiveGui(){
		for(Gui gui : guisToRender){
			if(gui.canBeActive()){
				return gui;
			}
		}
		return null;
	}

	private void setActiveGui(Gui gui){
		
		boolean flag = true;
		if(activeGui != null && activeGui.isAlwaysActive()){
			flag = false;
		}

		if(gui.canBeActive() && flag){
			gui.setActive(true);
			if(activeGui != null){
				activeGui.setActive(false);
			}
			activeGui = gui;
		}
	}

	@Override
	public void onCharTyped(char character) {
		if(activeGui != null){
			activeGui.onCharTyped(character);
		}

	}

	@Override
	public void onMouseMoved(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		if(activeGui != null){
			activeGui.onMouseMoved(mouseX, mouseY);
		}
	}

	@Override
	public void onMouseClicked(int button, int action, int mods) {

		boolean switchedActiveGui = false;
		boolean isGuiAlwaysActive = false;
		
		if(button == 0 && action == 1 && !guisToRender.isEmpty()){
			boolean isClickOnActiveGui = false;
			if(activeGui != null){
				isClickOnActiveGui = activeGui.isPosInGui(mouseX, mouseY);
				isGuiAlwaysActive = activeGui.isAlwaysActive();
			}

			if(!isClickOnActiveGui && !isGuiAlwaysActive){
				for(Gui gui : guisToRender){
					if(gui.canBeActive() && gui.isPosInGui(mouseX, mouseY)){
						setActiveGui(gui);
						sortGuis();
						switchedActiveGui = true;
						break;
					}
				}
				if(!switchedActiveGui){
					activeGui = null;
					checkForActiveGui();
				}
			}
		}

		if(!switchedActiveGui && activeGui != null){
			activeGui.onMouseClicked(button, action, mods);
		}
	}

	@Override
	public void onKeyAction(int keyCode, int scanCode, int mod, int action) {
		if(activeGui != null){
			activeGui.onKeyAction(keyCode, scanCode, mod, action);
		}
	}
}
