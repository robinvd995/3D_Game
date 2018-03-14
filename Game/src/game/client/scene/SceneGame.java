package game.client.scene;

import game.client.Game;
import game.client.renderer.Camera;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.GuiHud;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.world.WorldClient;
import game.common.event.EventManager;

public class SceneGame implements IScene{

	private WorldClient world;
	
	private Camera camera;
	
	public SceneGame(){}
	
	@Override
	public void initScene() {
		camera = new Camera();
		
		world = new WorldClient();
		//world = new ClientWorld(worldGen.generateFlatWorld(32, 32));
		//world.checkBlocksToRender();
		
		GuiHud testGui = new GuiHud(0, 0);
		EventManager.postPreUpdateEvent(new GuiOpenEvent((testGui)));
	}

	@Override
	public void update(double delta) {
		camera.update(delta);
		world.update(delta);
	}

	@Override
	public void fixedUpdate(double delta) {
		world.fixedUpdate(delta);
	}

	@Override
	public void closeScene() {
		RenderManager renderer = Game.INSTANCE.getRenderManager();
		renderer.getGuiRenderer().closeAllGuis();
		
	}

	@Override
	public void renderScene(RenderManager renderer, double delta) {
		renderer.preRender();
		renderer.renderWorld(camera, world, delta);
		renderer.renderGuis();
	}

}
