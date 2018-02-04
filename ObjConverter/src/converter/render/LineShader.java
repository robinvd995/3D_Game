package converter.render;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;

public class LineShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/converter/shaders/LineShader.vert";
	private static final String FRAGMENT_FILE = "src/converter/shaders/LineShader.frag";

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_lineColor;
	
	public LineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "vertexPosition");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("modelMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_lineColor = super.getUniformLocation("lineColor");
	}
	
	public void loadMVP(Matrix4f modelMatrix, Matrix4f viewMatrix, Matrix4f projectionMatrix){
		super.loadMatrix(location_modelMatrix, modelMatrix);
		super.loadMatrix(location_viewMatrix, viewMatrix);
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadLineColor(Vector3f color){
		super.loadVector(location_lineColor, color);
	}
}