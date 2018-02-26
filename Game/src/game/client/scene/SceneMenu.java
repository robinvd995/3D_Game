package game.client.scene;

import game.client.Game;
import game.client.network.Client;
import game.client.network.ConnectionStatus;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.Gui;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.gui.menu.GuiMainMenu;
import game.common.event.EventManager;
import game.common.event.SetSceneEvent;

public class SceneMenu implements IScene{
	
	private Gui menuGui;
	
	@Override
	public void initScene() {
		menuGui = new GuiMainMenu();
		EventManager.postPreUpdateEvent(new GuiOpenEvent(menuGui));
	}

	@Override
	public void update(double delta) {
		
	}

	@Override
	public void fixedUpdate(double delta) {
		if(Client.hasClient() && Client.getConnectionStatus() == ConnectionStatus.CONNECTED){
			SceneMultiplayer sceneMultiplayer = new SceneMultiplayer();
			EventManager.postPostUpdateEvent(new SetSceneEvent(sceneMultiplayer));
		}
	}

	@Override
	public void renderScene(RenderManager renderer, double delta) {
		renderer.preRender();
		renderer.renderGuis();
	}

	@Override
	public void closeScene() {
		RenderManager renderer = Game.INSTANCE.getRenderManager();
		renderer.getGuiRenderer().closeAllGuis();
	}
}