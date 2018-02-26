package game.client.renderer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import caesar.util.Matrix4f;
import game.client.display.DisplayManager;
import game.client.renderer.debug.IDebuggable;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;

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
		model.createDataBuffer("vertices", 0, 3, 24);
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
			model.updateDataBuffer("vertices", 3, entry.getVertexPositions());
			GL11.glDrawElements(GL11.GL_LINES, model.getSize(), GL11.GL_UNSIGNED_INT, 0);
		}
	}
	
	public void end(){
		GL20.glDisableVertexAttribArray(0);
		model.unbindModel();
		objectsToRender.clear();
		shader.stop();
	}
}  