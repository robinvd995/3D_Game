package game.display;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import caesar.util.AngleHelper;
import caesar.util.Matrix4f;
import caesar.util.Vector2f;
import game.input.InputManager;

public class DisplayManager {
	
	public static final DisplayManager INSTANCE = new DisplayManager();
	
	private EnumDisplaySize windowSize;
	
	private int resolution = 720;
	private float fov = 85;
	private float farPlane = 1000.0f;
	private float nearPlane = 0.1f;
	
	public GLFWErrorCallback errorCallback;
	public GLFWKeyCallback   keyCallback;
	public GLFWCursorPosCallback cursorCallback;
	public GLFWMouseButtonCallback mouseButtonCallback;
	
	private long window;
	
	private Matrix4f projectionMatrix;
	
	private List<IDisplaySizeListener> displaySizeListeners = new ArrayList<IDisplaySizeListener>();
	
	public void initDisplay(final String displayText){
		
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
		
		if(glfwInit() != GLFW_TRUE){
			throw new IllegalStateException("Unable to initialize GLFW!");
		}

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		windowSize = EnumDisplaySize.SIZE_1024_768;
		window = glfwCreateWindow(windowSize.displayWidth, windowSize.displayHeight, displayText, NULL, NULL);
		if(window == NULL)
			throw new RuntimeException("Failed to create the GLFW Window!");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				//System.out.println("key= " + key + ", scancode= " + scancode + ", action=" + action + ", mods= " + mods);
				InputManager.invoke(key, scancode, action, mods);
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
		
		initWindowResolution();
		
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		initViewport();
		
		setCursorActive(false);
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
		windowSize = displaySize;
		resolution = displaySize.displayHeight;
		glfwSetWindowSize(window, windowSize.displayWidth, windowSize.displayHeight);
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
				(vidmode.width() - windowSize.displayWidth) / 2,
				(vidmode.height() - windowSize.displayHeight) / 2
				);

		glfwMakeContextCurrent(window);
		calculateProjectionMatrix();
	}
	
	public void initViewport(){
		GL11.glViewport(0, 0, windowSize.displayWidth, windowSize.displayHeight);
	}
	
	public int getDisplayWidth(){
		return windowSize.displayWidth;
	}
	
	public int getDisplayHeight(){
		return windowSize.displayHeight;
	}
	
	public long getWindow(){
		return window;
	}
	
	public int getResolution(){
		return resolution;
	}
	
	public int getWidthResolution(){
		return (int) (windowSize.getAspectRatio() * resolution);
	}
	
	public void nextDisplaySize(){
		setWindowSize(windowSize.next());
	}

	public Vector2f getNormalizedDeviceCoords(float x, float y){
		float nx = (2.0f * x) / windowSize.displayWidth - 1.0f;
		float ny = (2.0f * y) / windowSize.displayWidth - 1.0f;
		return new Vector2f(nx, ny);
	}
	
	private void calculateProjectionMatrix(){
		/*float aspectRatio = (float)windowSize.displayWidth / (float) windowSize.displayHeight;
		int width = (int) (aspectRatio * resolution) / 2;
		int height = resolution / 2;
		
		projectionMatrix = MathHelper.createOrthographicMatrix(-width, width, -height, height);*/
		float aspectRatio = windowSize.getAspectRatio();
		float yScale = ((1F / AngleHelper.tan(AngleHelper.toRadians(fov / 2F))) * aspectRatio);
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
		return new Vector2f(windowSize.displayWidth / 2.0f, windowSize.displayHeight / 2.0f);
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
}