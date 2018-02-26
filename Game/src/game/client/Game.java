package game.client;

import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import com.google.common.eventbus.Subscribe;

import game.client.display.DisplayManager;
import game.client.input.InputManager;
import game.client.network.Client;
import game.client.renderer.RenderManager;
import game.client.scene.IScene;
import game.client.scene.SceneMenu;
import game.common.event.EventManager;
import game.common.event.SetSceneEvent;

public class Game {

	public static final String GAME_TITLE = "3D OpenGL Game";
	public static final String GAME_VERSION = "0.0.1";
	public static final int GAME_FPS = 60;

	public static final Game INSTANCE = new Game();

	//private SceneManager sceneManager;
	
	public Timer timer;

	private RenderManager renderer;
	
	private boolean playerMouseInputDisabled = false;
	private boolean playerKeyInputDisabled = false;
	
	private IScene scene;
	
	private Game(){}

	protected void init(){
		DisplayManager.INSTANCE.initDisplay(GAME_TITLE + " version: " + GAME_VERSION);
		DisplayManager.INSTANCE.setBackgroundColor(0.0F, 0.0F, 0.0F, 0.0F);

		renderer = new RenderManager();
		renderer.initOpenGL();
		renderer.initRenderer();

		timer = new Timer(GAME_FPS);
		timer.setInitialTime();
		timer.setFpsTracking(true);
		
		/*sceneManager = new SceneManager();
		sceneManager.init();
		sceneManager.loadScene("main_menu");*/
		
		scene = new SceneMenu();
		scene.initScene();
		
		EventManager.registerEventListener(INSTANCE);
	}

	protected void run(){
		glfwSwapBuffers(DisplayManager.INSTANCE.getWindow());
		glfwPollEvents();

		EventManager.firePreUpdateEvents();
		
		if(timer.update()){
			scene.fixedUpdate(timer.getTickTime());
		}

		scene.update(timer.getDeltaTime());
		InputManager.end();

		scene.renderScene(renderer, timer.getDeltaTime());
		
		EventManager.firePostUpdateEvents();
	}

	protected boolean shouldClose(){
		return glfwWindowShouldClose(DisplayManager.INSTANCE.getWindow()) == GLFW_TRUE;
	}

	protected void close(){
		renderer.cleanUp();
		scene.closeScene();
		
		Client.stopClient();
	}
	
	public void setMouseDisabled(boolean active){
		this.playerMouseInputDisabled = active;
		DisplayManager.INSTANCE.setCursorActive(active);
	}
	
	public boolean isMouseDisabled(){
		return playerMouseInputDisabled;
	}
	
	public void setMovementDisabled(boolean active){
		this.playerKeyInputDisabled = active;
	}
	
	public boolean isPlayerMovementDisabled(){
		return playerKeyInputDisabled;
	}
	
	public RenderManager getRenderManager(){
		return renderer;
	}
	
	@Subscribe
	public void onSetSceneEvent(SetSceneEvent event){
		scene.closeScene();
		scene = event.getScene();
		scene.initScene();
	}
}
