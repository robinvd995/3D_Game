package converter.ui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import converter.api.model.IndexedModel;
import converter.io.IOManager;
import converter.model.LoadedModel;
import converter.model.ModelBuilder;
import converter.model.ModelLoader;
import converter.model.RawModel;
import converter.model.modifier.IModelModifier;
import converter.render.Camera;
import converter.render.LineShader;
import converter.render.StaticShader;

public class ModelScene {

	private IndexedModel theModel;
	private ModelBuilder modelBuilder;
	private boolean modelNeedsLoading = false;
	private LoadedModel loadedModel;

	private int screenWidth = 1;
	private int screenHeight = 1;
	private int fov = 70;
	private float nearPlane = 0.1F;
	private float farPlane = 1000.0F;

	private boolean screenNeedsUpdating = true;

	private StaticShader staticShader;
	private LineShader lineShader;

	private ModelLoader modelLoader;

	private int texture;
	private int linesVAO;

	private Camera camera;

	private List<IModelModifier> modifiers = new ArrayList<IModelModifier>();

	public ModelScene(){
		modelLoader = new ModelLoader();
		camera = new Camera();
	}

	public void initOpenGL(){
		staticShader = new StaticShader();
		lineShader = new LineShader();
		GL11.glClearColor(0.3F, 0.3F, 0.3F, 1.0F);
		texture = IOManager.loadTexture("test");
		linesVAO = modelLoader.loadLines(new float[]{
				-10.0f, 0.0f, 10.0f, 0.0f,
				0.0f, -10.0f, 0.0f, 10.0f,
				-10.0f, 10.0f, 10.0f, 10.0f,
				-10.0f, -10.0f, 10.0f, -10.0f,
				10.0f, -10.0f, 10.0f, 10.0f,
				-10.0f, -10.0f, -10.0f, 10.0f,

				-10.0f, 2.0f, 10.0f, 2.0f,
				-10.0f, 4.0f, 10.0f, 4.0f,
				-10.0f, 6.0f, 10.0f, 6.0f,
				-10.0f, 8.0f, 10.0f, 8.0f,

				-10.0f, -2.0f, 10.0f, -2.0f,
				-10.0f, -4.0f, 10.0f, -4.0f,
				-10.0f, -6.0f, 10.0f, -6.0f,
				-10.0f, -8.0f, 10.0f, -8.0f,

				2.0f, -10.0f, 2.0f, 10.0f,
				4.0f, -10.0f, 4.0f, 10.0f,
				6.0f, -10.0f, 6.0f, 10.0f,
				8.0f, -10.0f, 8.0f, 10.0f,

				-2.0f, -10.0f, -2.0f, 10.0f,
				-4.0f, -10.0f, -4.0f, 10.0f,
				-6.0f, -10.0f, -6.0f, 10.0f,
				-8.0f, -10.0f, -8.0f, 10.0f,
		});
		camera.moveCamera(45.0F, 22.5F, 10.0f);
	}

	public void renderScene(){

		if(Mouse.isButtonDown(1)){
			camera.moveCamera(Mouse.getDX(), Mouse.getDY(), 0.0F);
		}

		camera.moveCamera(0.0f, 0.0f, -Mouse.getDWheel() * 0.25f);

		if(screenNeedsUpdating){
			this.screenNeedsUpdating = false;
			GL11.glViewport(0, 0, screenWidth, screenHeight);
		}

		if(modelNeedsLoading){
			this.modelNeedsLoading = false;
			this.loadedModel = modelLoader.loadToVAO(theModel);
		}

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		Matrix4f viewMatrix = camera.createViewMatrix();//MathHelper.createViewMatrix(new Vector3f(-20.0F, 20.0F, 20.0F), Quaternion.fromVector(new Vector3f(22.5f, 45.0f, 0.0f)));
		Matrix4f lineMatrix = MathHelper.createTransformationMatrix(new Vector3f(0F, 0.0F, 0.0F), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f(0.0f, 0.0f, 0.0f)));
		Matrix4f projectionMatrix = MathHelper.createProjectionMatrix(screenWidth, screenHeight, fov, nearPlane, farPlane);

		lineShader.start();
		GL30.glBindVertexArray(linesVAO);
		GL20.glEnableVertexAttribArray(0);
		lineShader.loadMVP(lineMatrix, viewMatrix, projectionMatrix);
		lineShader.loadLineColor(new Vector3f(1.0F, 0.0F, 0.0F));
		GL11.glDrawArrays(GL11.GL_LINES, 0, 12);
		lineShader.loadLineColor(new Vector3f(0.0F, 0.0F, 1.0F));
		GL11.glDrawArrays(GL11.GL_LINES, 12, 32);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		lineShader.stop();

		if(loadedModel != null){

			Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(0F, 0.0F, 0.0F), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f(0.0f, 0.0f, 0.0f)));

			GL11.glEnable(GL11.GL_DEPTH_TEST);

			staticShader.start();

			staticShader.loadMVP(modelMatrix, viewMatrix, projectionMatrix);
			staticShader.loadLight(new Vector3f(0,10,10), new Vector3f(1.0F, 1.0F, 1.0F));

			int vao = loadedModel.getVao();

			GL30.glBindVertexArray(vao);

			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

			loadedModel.renderAll();
			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);

			GL30.glBindVertexArray(0);

			staticShader.stop();
		}
	}

	public void addModifier(IModelModifier modifier){
		this.modifiers.add(modifier);
	}

	public void setBounds(int width, int height){
		this.screenWidth = width;
		this.screenHeight = height;
		this.screenNeedsUpdating = true;
	}

	public void loadModel(RawModel model){
		modelBuilder = ModelBuilder.buildFromObj(model);
		buildModel();
	}
	
	public void loadModel(IndexedModel model){
		modelBuilder = ModelBuilder.buildFromIndexedModel(model);
		buildModel();
	}

	public void buildModel(){
		theModel = modelBuilder.buildModel();
		this.modelNeedsLoading = true;
	}
	
	public IndexedModel getModel(){
		return theModel;
	}

	public void onClosing(){

	}
}
