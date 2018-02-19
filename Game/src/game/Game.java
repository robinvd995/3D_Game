package game;

import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import caesar.util.Vector3f;
import game.display.DisplayManager;
import game.input.IKeyListener;
import game.input.IMouseListener;
import game.input.InputManager;
import game.renderer.Camera;
import game.renderer.RenderManager;
import game.renderer.gui.Gui;
import game.world.ClientWorld;
import game.world.WorldGenerator;

public class Game {

	public static final String GAME_TITLE = "3D OpenGL Game";
	public static final String GAME_VERSION = "0.0.1";
	public static final int GAME_FPS = 60;

	public static final Game INSTANCE = new Game();

	public Timer timer;

	private ClientWorld world;

	private RenderManager renderer;
	private Camera camera;

	private Vector3f lightDirection = new Vector3f(45.0f, 90.0f, 0.0f);
	
	private boolean playerMouseInputDisabled = false;
	private boolean playerKeyInputDisabled = false;
	
	private Game(){}

	protected void init(){
		DisplayManager.INSTANCE.initDisplay(GAME_TITLE + " version: " + GAME_VERSION);
		DisplayManager.INSTANCE.setBackgroundColor(0.0F, 0.0F, 0.0F, 0.0F);

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
		timer.setFpsTracking(true);
	}

	protected void run(){
		glfwSwapBuffers(DisplayManager.INSTANCE.getWindow());
		glfwPollEvents();

		if(timer.update()){
			/*double newLightX = lightDirection.getX() + (20.0f * timer.getDeltaTime());
			if(newLightX > 180.0f){
				newLightX -= 360.0f;
			}
			lightDirection.setX((float) newLightX);*/

		}

		camera.update(timer.getDeltaTime());
		world.getWorldObj().update(timer.getDeltaTime());
		InputManager.end();

		renderer.renderWorld(camera, world, timer.getDeltaTime(), lightDirection);
	}

	protected boolean shouldClose(){
		return glfwWindowShouldClose(DisplayManager.INSTANCE.getWindow()) == GLFW_TRUE;
	}

	protected void close(){
		renderer.cleanUp();
	}

	public void openGui(Gui gui){
		
		boolean isGuiOpen = renderer.getGuiRenderer().isGuiOpen(gui);
		
		if(!isGuiOpen){
			renderer.getGuiRenderer().addGui(gui);
			if(gui instanceof IMouseListener){
				InputManager.addMouseListener((IMouseListener) gui);
			}
			if(gui instanceof IKeyListener){
				InputManager.addKeyListener((IKeyListener) gui);
			}
		}
		else{
			Gui openGui = renderer.getGuiRenderer().getGui(gui.getGuiId());
			renderer.getGuiRenderer().closeGui(openGui);
			if(openGui instanceof IMouseListener){
				InputManager.removeMouseListener((IMouseListener) openGui);
			}
			if(openGui instanceof IKeyListener){
				InputManager.removeKeyListener((IKeyListener) openGui);
			}
		}
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
}
