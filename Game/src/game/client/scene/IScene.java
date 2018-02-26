package game.client.scene;

import game.client.renderer.RenderManager;

public interface IScene {

	void initScene();
	void update(double delta);
	void fixedUpdate(double delta);
	void renderScene(RenderManager renderer, double delta);
	void closeScene();
}
