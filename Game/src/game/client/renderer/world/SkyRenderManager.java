package game.client.renderer.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.client.display.DisplayManager;
import game.client.geometry.GeometryBuilder;
import game.client.renderer.GLHelper;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;
import game.client.renderer.texture.LoadedTexture;
import game.client.renderer.texture.TextureData;
import game.client.renderer.texture.TextureLoader;
import game.client.renderer.texture.TextureManager;

public class SkyRenderManager {

	private static final float SUN_DISTANCE = 400.0f;
	private static final float SUN_SCALE = 20.0f;

	private boolean shouldRenderSun = true;

	private SimpleModel skyboxCube;
	private LoadedTexture skyboxTexture;
	private Shader skyboxShader;

	private SimpleModel sunQuad;
	private TextureData sunTextureData = TextureManager.getDefaultGuiTexture("sun", 256, 256);
	private Shader sunShader;

	public SkyRenderManager(){
		skyboxCube = ModelLoader.INSTANCE.loadSimpleCube(GeometryBuilder.buildCenteredCube(500.0f));
		skyboxTexture = TextureLoader.loadCubeMap("skybox");

		sunQuad = ModelLoader.INSTANCE.loadSimpleQuad(GeometryBuilder.buildCenteredQuad(1.0f));
	}

	public void initRenderer(){
		skyboxShader = ShaderBuilder.newInstance("skybox").buildShader();
		sunShader = ShaderBuilder.newInstance("sun").buildShader();
	}

	public void renderSun(WorldClient world, Matrix4f viewMatrix){
		GLHelper.enableAlphaBlending();
		
		sunShader.start();
		sunShader.loadVector3f("color", world.getLightColor());
		Vector3f sunPosition = Vector3f.mult(world.getLightDirection(), SUN_DISTANCE);
		Matrix4f mvp = calculateMvpMatrix(sunPosition, viewMatrix);
		sunShader.loadMatrix("mvpMatrix", mvp);
		TextureManager.bindTexture(sunTextureData);
		sunQuad.bindModel();
		sunShader.enableAttribArrays();

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

		sunShader.disableAttribArrays();
		sunQuad.unbindModel();
		sunShader.stop();
		
		GLHelper.disableBlending();
	}

	public void renderSkybox(Matrix4f viewMatrix){
		skyboxShader.start();
		skyboxShader.loadMatrix("projectionMatrix", DisplayManager.INSTANCE.getProjectionMatrix());
		Matrix4f viewMatrixCopy = new Matrix4f(viewMatrix);
		viewMatrixCopy.m30 = 0.0f;
		viewMatrixCopy.m31 = 0.0f;
		viewMatrixCopy.m32 = 0.0f;
		skyboxShader.loadMatrix("viewMatrix", viewMatrixCopy);
		skyboxCube.bindModel();
		skyboxShader.enableAttribArrays();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skyboxTexture.getTextureID());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skyboxCube.getSize());
		skyboxShader.disableAttribArrays();
		skyboxCube.unbindModel();
		skyboxShader.stop();
	}

	private Matrix4f calculateMvpMatrix(Vector3f sunPos, Matrix4f viewMatrix){
		Matrix4f modelMatrix = new Matrix4f();
		Matrix4f.translate(sunPos, modelMatrix, modelMatrix);
		Matrix4f modelViewMat = applyViewMatrix(modelMatrix, viewMatrix);
		Matrix4f.scale(new Vector3f(SUN_SCALE, SUN_SCALE, SUN_SCALE), modelViewMat, modelViewMat);
		return Matrix4f.mul(DisplayManager.INSTANCE.getProjectionMatrix(), modelViewMat, null);
	}

	private Matrix4f applyViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix){
		Matrix4f viewMatrixCopy = new Matrix4f(viewMatrix);
		viewMatrixCopy.m30 = 0.0f;
		viewMatrixCopy.m31 = 0.0f;
		viewMatrixCopy.m32 = 0.0f;
		modelMatrix.m00 = viewMatrixCopy.m00;
		modelMatrix.m01 = viewMatrixCopy.m10;
		modelMatrix.m02 = viewMatrixCopy.m20;
		modelMatrix.m10 = viewMatrixCopy.m01;
		modelMatrix.m11 = viewMatrixCopy.m11;
		modelMatrix.m12 = viewMatrixCopy.m21;
		modelMatrix.m20 = viewMatrixCopy.m02;
		modelMatrix.m21 = viewMatrixCopy.m12;
		modelMatrix.m22 = viewMatrixCopy.m22;
		return Matrix4f.mul(viewMatrix, modelMatrix, null);
	}
}
