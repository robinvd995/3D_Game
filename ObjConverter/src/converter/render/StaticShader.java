package converter.render;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/converter/shaders/StaticShader.vert";
	private static final String FRAGMENT_FILE = "src/converter/shaders/StaticShader.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_lightPosition;
	private int location_lightColor;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
		super.bindAttribute(1, "vertexUV");
		super.bindAttribute(2, "vertexNormal");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
	}
	
	public void loadMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix){
		super.loadMatrix(location_modelMatrix, modelMatrix);
		super.loadMatrix(location_viewMatrix, viewMatrix);
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadLight(Vector3f pos, Vector3f color){
		super.loadVector(location_lightPosition, pos);
		super.loadVector(location_lightColor, color);
	}
}