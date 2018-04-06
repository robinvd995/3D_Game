package game.client.display;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import caesar.util.AngleHelper;
import caesar.util.Matrix4f;
import caesar.util.Vector2f;
import game.client.config.DisplayConfig;
import game.client.input.InputManager;
import game.common.json.JsonLoader;

public class DisplayManager {

	public static final DisplayManager INSTANCE = new DisplayManager();

	private DisplayConfig config;
	private int resolution = 720;
	private float farPlane = 1000.0f;
	private float nearPlane = 0.1f;

	public GLFWErrorCallback errorCallback;
	public GLFWKeyCallback   keyCallback;
	public GLFWCursorPosCallback cursorCallback;
	public GLFWMouseButtonCallback mouseButtonCallback;
	public GLFWCharCallback charCallback;
	public GLFWScrollCallback scrollCallback;

	private long window;

	private Matrix4f projectionMatrix;

	private List<IDisplaySizeListener> displaySizeListeners = new ArrayList<IDisplaySizeListener>();

	public void initDisplay(final String displayText){

		loadDisplayConfig();

		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		if(glfwInit() != GLFW_TRUE){
			throw new IllegalStateException("Unable to initialize GLFW!");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		glfwWindowHint(GLFW_REFRESH_RATE, GLFW_DONT_CARE);
		long monitor = NULL;
		if(config.getDisplayMode() == EnumDisplayMode.FULLSCREEN){
			monitor = glfwGetPrimaryMonitor();
		}

		window = glfwCreateWindow(config.getDisplaySize().displayWidth, config.getDisplaySize().displayHeight, displayText, monitor, NULL);
		if(window == NULL)
			throw new RuntimeException("Failed to create the GLFW Window!");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				//System.out.println("key= " + key + ", scancode= " + scancode + ", action=" + action + ", mods= " + mods);
				InputManager.invokeKey(key, scancode, action, mods);
			}
		});

		GLFW.glfwSetCursorPosCallback(window, cursorCallback = new GLFWCursorPosCallback(){

			@Override
			public void invoke(long window, double posX, double posY) {
				InputManager.invokeMousePos(posX, posY);
			}

		});

		GLFW.glfwSetMouseButtonCallback(window, mouseButtonCallback = new GLFWMouseButtonCallback(){

			@Override
			public void invoke(long window, int button, int action, int mods) {
				InputManager.invokeMouseButton(button, action, mods);
			}

		});

		GLFW.glfwSetCharCallback(window, charCallback = new GLFWCharCallback(){

			@Override
			public void invoke(long window, int c) {
				InputManager.invokeCharType((char) c);
			}

		});

		GLFW.glfwSetScrollCallback(window, scrollCallback = new GLFWScrollCallback(){

			@Override
			public void invoke(long window, double xoffset, double yoffset) {
				InputManager.invokeScroll(xoffset, yoffset);
			}

		});

		initWindowResolution();

		glfwSwapInterval(1);
		glfwShowWindow(window);

		GL.createCapabilities();

		initViewport();

		setCursorActive(false);
	}

	private void loadDisplayConfig(){
		try {
			config = JsonLoader.loadClassFromJson("config/display", DisplayConfig.class);
		} catch (FileNotFoundException e) {
			System.out.println("Could not find a display config file, loading default display settings!");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(config == null){
			config = new DisplayConfig();
			try {
				JsonLoader.saveJsonFile("config", "display", config);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void swapBuffer(){
		glfwSwapBuffers(window);
	}

	public void setBackgroundColor(float r, float g, float b){
		setBackgroundColor(r, g, b, 1.0F);
	}

	public void setBackgroundColor(float r, float g, float b, float a){
		GL11.glClearColor(r, g, b, a);
	}

	public void cleanUp(){
		errorCallback.release();
		glfwDestroyWindow(window);
		glfwTerminate();
	}

	public void setWindowSize(EnumDisplaySize displaySize){
		config.setDisplaySize(displaySize);
		resolution = displaySize.displayHeight;
		glfwSetWindowSize(window, displaySize.displayWidth, displaySize.displayHeight);
		initWindowResolution();
		initViewport();
		for(IDisplaySizeListener listener : displaySizeListeners){
			listener.onDisplaySizeChanged(INSTANCE);
		}
	}

	private void initWindowResolution(){
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(
				window,
				(vidmode.width() - getDisplayWidth()) / 2,
				(vidmode.height() - getDisplayHeight()) / 2
				);

		glfwMakeContextCurrent(window);
		calculateProjectionMatrix();
	}

	public void initViewport(){
		GL11.glViewport(0, 0, getDisplayWidth(), getDisplayHeight());
	}

	public int getDisplayWidth(){
		return config.getDisplaySize().displayWidth;
	}

	public int getDisplayHeight(){
		return config.getDisplaySize().displayHeight; 
	}

	public float getAspectRatio(){
		return config.getDisplaySize().getAspectRatio();
	}
	
	public long getWindow(){
		return window;
	}

	public int getResolution(){
		return resolution;
	}

	public int getWidthResolution(){
		return (int) (getAspectRatio() * resolution);
	}

	public void nextDisplaySize(){
		setWindowSize(config.getDisplaySize().next());
	}

	public Vector2f getNormalizedDeviceCoords(float x, float y){
		float nx = (2.0f * x) / getDisplayWidth() - 1.0f;
		float ny = (2.0f * y) / getDisplayHeight() - 1.0f;
		return new Vector2f(nx, ny);
	}

	private void calculateProjectionMatrix(){
		/*float aspectRatio = (float)windowSize.displayWidth / (float) windowSize.displayHeight;
		int width = (int) (aspectRatio * resolution) / 2;
		int height = resolution / 2;

		projectionMatrix = MathHelper.createOrthographicMatrix(-width, width, -height, height);*/
		float aspectRatio = getAspectRatio();
		float yScale = ((1F / AngleHelper.tan(AngleHelper.toRadians(config.getFov() / 2F))));
		float xScale = yScale / aspectRatio;
		float frustrumLength = farPlane - nearPlane;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = xScale;
		projectionMatrix.m11 = yScale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustrumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustrumLength);
		projectionMatrix.m33 = 0;
	}

	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}

	public Vector2f getScreenCenterCoordinates(){
		return new Vector2f(getDisplayWidth() / 2.0f, getDisplayHeight() / 2.0f);
	}

	public void setMousePosition(Vector2f position){
		GLFW.glfwSetCursorPos(window, position.getX(), position.getY());
	}

	public void addDisplaySizeListener(IDisplaySizeListener listener){
		displaySizeListeners.add(listener);
	}

	public void setCursorActive(boolean active){
		if(active){
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
		else{
			GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
		}
	}

	public void invokeCloseWindow(){
		GLFW.glfwSetWindowShouldClose(window, GLFW.GLFW_TRUE);
	}
}