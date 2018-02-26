package game.client.scene;

import game.client.renderer.RenderManager;

public class SceneMultiplayer implements IScene{
	
	public SceneMultiplayer(){
		
	}
	
	@Override
	public void initScene() {
		
	}

	@Override
	public void update(double delta) {
		
	}

	@Override
	public void fixedUpdate(double delta) {
		
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
