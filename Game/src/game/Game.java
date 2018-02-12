package game;

import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import caesar.util.Vector3f;
import game.display.DisplayManager;
import game.input.InputManager;
import game.renderer.Camera;
import game.renderer.RenderManager;
import game.world.ClientWorld;
import game.world.WorldGenerator;

public class Game {

	public static final String GAME_TITLE = "3D OpenGL Game";
	public static final String GAME_VERSION = "0.0.1";
	public static final int GAME_FPS = 60;

	public static final Game INSTANCE = new Game();
	
	private Timer timer;
	
	private ClientWorld world;
	
	private RenderManager renderer;
	private Camera camera;
	
	private Vector3f lightDirection = new Vector3f(45.0f, 90.0f, 0.0f);
	
	private Game(){}
	
	protected void init(){
		DisplayManager.INSTANCE.initDisplay(GAME_TITLE + " version: " + GAME_VERSION);
		DisplayManager.INSTANCE.setBackgroundColor(0.0F, 0.0F, 0.0F);
		
		renderer = new RenderManager();
		renderer.initOpenGL();
		renderer.initRenderer();
		camera = new Camera();
		
		WorldGenerator worldGen = new WorldGenerator();
		world = new ClientWorld(worldGen.generateWorld(64, 64, 0, 10, 5));
		//world = new ClientWorld(worldGen.generateFlatWorld(32, 32));
		world.checkBlocksToRender();
		
		timer = new Timer(GAME_FPS);
		timer.setInitialTime();
		timer.setFpsTracking(false);
	}
	
	protected void run(){
		glfwSwapBuffers(DisplayManager.INSTANCE.getWindow());
		glfwPollEvents();
		
		if(timer.update()){
			camera.update(timer.getTickTime());
			/*double newLightX = lightDirection.getX() + (20.0f * timer.getDeltaTime());
			if(newLightX > 180.0f){
				newLightX -= 360.0f;
			}
			lightDirection.setX((float) newLightX);*/
			world.getWorldObj().update(timer.getTickTime());
			InputManager.end();
		}
		
		renderer.renderWorld(camera, world, timer.getDeltaTime(), lightDirection);
	}
	
	protected boolean shouldClose(){
		return glfwWindowShouldClose(DisplayManager.INSTANCE.getWindow()) == GLFW_TRUE;
	}
	
	protected void close(){
		renderer.cleanUp();
	}
}
