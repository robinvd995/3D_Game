package game.client.renderer.shader;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import caesar.util.Vector4f;

public class BlockShader extends ShaderProgram {

	private static final String VERTEX_FILE = "res/shaders/block.vert";
	private static final String FRAGMENT_FILE = "res/shaders/block.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_blockColor;
	private int location_lightDirection;
	private int location_textureCoords;
	
	public BlockShader() {
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
		location_blockColor = super.getUniformLocation("blockColor");
		location_lightDirection = super.getUniformLocation("lightDirection");
		location_textureCoords = super.getUniformLocation("textureCoords");
	}
	
	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix(location_modelMatrix, modelMatrix);
	}
	
	public void loadViewMatrix(Matrix4f viewMatrix){
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix){
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix){
		super.loadMatrix(location_modelMatrix, modelMatrix);
		super.loadMatrix(location_viewMatrix, viewMatrix);
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadBlockColor(Vector3f color){
		super.loadVector3f(location_blockColor, color);
	}
	
	public void loadLightDirection(Vector3f dir){
		super.loadVector3f(location_lightDirection, dir);
	}
	
	public void loadTextureCoords(Vector4f coords){
		super.load4DVector(location_textureCoords, coords);
	}
}