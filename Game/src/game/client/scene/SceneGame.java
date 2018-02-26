package game.client.scene;

import caesar.util.Vector3f;
import game.client.Game;
import game.client.renderer.Camera;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.GuiHud;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.common.event.EventManager;
import game.common.world.ClientWorld;
import game.common.world.WorldGenerator;

public class SceneGame implements IScene{

	private ClientWorld world;
	
	private Camera camera;

	private Vector3f lightDirection = new Vector3f(45.0f, 90.0f, 0.0f);
	
	public SceneGame(){}
	
	@Override
	public void initScene() {
		camera = new Camera();
		
		WorldGenerator worldGen = new WorldGenerator();
		world = new ClientWorld(worldGen.generateWorld(64, 64, 0, 10, 5));
		//world = new ClientWorld(worldGen.generateFlatWorld(32, 32));
		world.checkBlocksToRender();
		
		GuiHud testGui = new GuiHud(0, 0);
		EventManager.postPreUpdateEvent(new GuiOpenEvent((testGui)));
	}

	@Override
	public void update(double delta) {
		camera.update(delta);
		world.getWorldObj().update(delta);
	}

	@Override
	public void fixedUpdate(double delta) {
		
	}

	@Override
	public void closeScene() {
		RenderManager renderer = Game.INSTANCE.getRenderManager();
		renderer.getGuiRenderer().closeAllGuis();
		
	}

	@Override
	public void renderScene(RenderManager renderer, double delta) {
		renderer.preRender();
		renderer.renderWorld(camera, world, delta, lightDirection);
		renderer.renderGuis();
		
	}

}
