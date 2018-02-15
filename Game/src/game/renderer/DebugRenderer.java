package game.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL20;

import caesar.util.Matrix4f;
import game.display.DisplayManager;
import game.renderer.debug.IDebuggable;
import game.renderer.model.ModelLoader;
import game.renderer.model.StreamModel;
import game.renderer.shader.LineShader;
import game.renderer.shader.Shader;
import game.renderer.shader.ShaderBuilder;

public class DebugRenderer {

	public static final DebugRenderer INSTANCE = new DebugRenderer();
	
	//private LineShader shader;
	private Shader shader;
	private StreamModel model;
	
	private List<IDebuggable> objectsToRender = new ArrayList<IDebuggable>();
	
	private DebugRenderer(){}
	
	protected void init(){
		shader = ShaderBuilder.buildShader("line");
		model = ModelLoader.INSTANCE.createStreamModel();
		model.bindModel();
		model.createIndexBuffer(24);
		model.createDataBuffer("vertices", 24);
		model.unbindModel();
	}
	
	public void addObjectToRender(IDebuggable debuggable){
		objectsToRender.add(debuggable);
	}
	
	protected void prepare(Matrix4f viewMatrix){
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();
		
		shader.start();
		shader.loadMatrix("projectionMatrix", projectionMatrix);
		shader.loadMatrix("viewMatrix", viewMatrix);
		
		model.bindModel();
		GL20.glEnableVertexAttribArray(0);
	}
	
	protected void renderAll(){
		for(IDebuggable entry : objectsToRender){
			shader.loadMatrix("modelMatrix", entry.getTransformationMatrix());
			shader.loadVector3f("lineColor", entry.getLineColor());
			model.updateIndexBuffer(entry.getIndices());
			model.updateDataBuffer("vertices", entry.getVertexPositions());
			model.render(entry.getIndices().length);
		}
	}
	
	public void end(){
		GL20.glDisableVertexAttribArray(0);
		model.unbindModel();
		objectsToRender.clear();
		shader.stop();
	}
}  