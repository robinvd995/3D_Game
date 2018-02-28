package game.client.scene;

import game.client.network.Client;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.gui.menu.GuiConnectionTest;
import game.common.event.EventManager;

public class SceneMultiplayer implements IScene{
	
	public SceneMultiplayer(){
		
	}
	
	@Override
	public void initScene() {
		EventManager.postPostUpdateEvent(new GuiOpenEvent(new GuiConnectionTest()));
	}

	@Override
	public void update(double delta) {
		
	}

	@Override
	public void fixedUpdate(double delta) {
		Client.tickClient();
	}

	@Override
	public void renderScene(RenderManager renderer, double delta) {
		renderer.preRender();
		renderer.renderGuis();
	}

	@Override
	public void closeScene() {
		
	}

}
