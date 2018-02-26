package game.client.renderer.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import caesar.util.Matrix4f;
import game.client.display.DisplayManager;
import game.client.geometry.GeometryBuilder;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.model.SimpleModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;
import game.client.renderer.texture.LoadedTexture;
import game.client.renderer.texture.TextureLoader;

public class SkyboxRenderManager {

	private SimpleModel skyboxCube;
	private LoadedTexture skyboxTexture;
	private Shader shader;
	
	public SkyboxRenderManager(){
		skyboxCube = ModelLoader.INSTANCE.loadSimpleCube(GeometryBuilder.buildCenteredCube(500.0f));
		skyboxTexture = TextureLoader.loadCubeMap("skybox");
	}
	
	public void initRenderer(){
		shader = ShaderBuilder.buildShader("skybox");
	}
	
	public void renderSkybox(Matrix4f viewMatrix){
		shader.start();
		shader.loadMatrix("projectionMatrix", DisplayManager.INSTANCE.getProjectionMatrix());
		Matrix4f viewMatrixCopy = new Matrix4f(viewMatrix);
		viewMatrixCopy.m30 = 0.0f;
		viewMatrixCopy.m31 = 0.0f;
		viewMatrixCopy.m32 = 0.0f;
		shader.loadMatrix("viewMatrix", viewMatrixCopy);
		skyboxCube.bindModel();
		shader.enableAttribArrays();
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skyboxTexture.getTextureID());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, skyboxCube.getSize());
		shader.disableAttribArrays();
		skyboxCube.unbindModel();
		shader.stop();
	}
}
