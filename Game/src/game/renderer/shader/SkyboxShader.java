package game.renderer.shader;

public class SkyboxShader extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/block.vert";
	private static final String FRAGMENT_FILE = "res/shaders/block.frag";
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
	}
	
	@Override
	protected void getAllUniformLocations() {
		
	}

}
