package game.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL20;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.display.DisplayManager;
import game.renderer.debug.IDebuggable;
import game.renderer.model.ModelLoader;
import game.renderer.model.StreamModel;
import game.renderer.shader.LineShader;

public class DebugRenderer {

	public static final DebugRenderer INSTANCE = new DebugRenderer();
	
	private LineShader shader;
	private StreamModel model;
	
	private List<IDebuggable> objectsToRender = new ArrayList<IDebuggable>();
	
	private DebugRenderer(){}
	
	protected void init(){
		shader = new LineShader();
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
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadViewMatrix(viewMatrix);
		
		model.bindModel();
		GL20.glEnableVertexAttribArray(0);
	}
	
	protected void renderAll(){
		for(IDebuggable entry : objectsToRender){
			shader.loadModelMatrix(entry.getTransformationMatrix());
			shader.loadLineColor(entry.getLineColor());
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