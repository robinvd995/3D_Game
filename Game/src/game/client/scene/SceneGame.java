package game.client.scene;

import game.client.Game;
import game.client.renderer.RenderManager;
import game.client.renderer.gui.GuiHud;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.world.WorldClient;
import game.common.entity.Player;
import game.common.event.EventManager;

public class SceneGame implements IScene{

	private WorldClient world;
	
	private static Player player;
	
	public SceneGame(){}
	
	@Override
	public void initScene() {
		
		player = new Player();
		player.getTransform().getPosition().set(0, 16, 0);
		player.init();
		
		world = new WorldClient(player);
		world.init();
		//world = new ClientWorld(worldGen.generateFlatWorld(32, 32));
		//world.checkBlocksToRender();
		
		world.spawnEntity(player);
		
		GuiHud testGui = new GuiHud(player);
		EventManager.postPreUpdateEvent(new GuiOpenEvent((testGui)));
	}

	@Override
	public void update(double delta) {
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
		renderer.renderWorld(player.getCamera(), world, delta);
		renderer.renderGuis();
	}

	public static Player getPlayer(){
		return player;
	}
}
