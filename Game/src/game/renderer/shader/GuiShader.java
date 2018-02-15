package game.renderer.shader;

import caesar.util.Vector2f;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/gui.vert";
	private static final String FRAGMENT_FILE = "res/shaders/gui.frag";
	
	private int location_elementPosition;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
		super.bindAttribute(1, "vertexUV");
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_elementPosition = super.getUniformLocation("elementPosition");
	}
	
	public void loadElementPosition(Vector2f elementPosition){
		super.load2DVector(location_elementPosition, elementPosition);
	}
}
