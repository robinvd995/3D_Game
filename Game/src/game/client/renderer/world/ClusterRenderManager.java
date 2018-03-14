package game.client.renderer.world;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Quaternion;
import caesar.util.Vector3f;
import caesar.util.Vector4f;
import game.client.display.DisplayManager;
import game.client.renderer.GLHelper;
import game.client.renderer.model.StreamModel;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;
import game.client.renderer.texture.TextureManager;
import game.common.world.cluster.ClusterPosition;

public class ClusterRenderManager {

	private Shader shader;

	public ClusterRenderManager(){
		shader = ShaderBuilder.buildShader("cluster");
	}

	public void initRenderer(){
		shader.start();
		shader.loadInt("diffuseSampler", 0);
		shader.loadInt("specularSampler", 1);
		shader.loadInt("normalSampler", 2);
		shader.stop();
	}

	public void prepare(WorldClient world, Matrix4f viewMatrix) {
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();

		shader.start();
		shader.loadMatrix("viewMatrix", viewMatrix);
		shader.loadMatrix("projectionMatrix", projectionMatrix);

		shader.loadVector3f("lightDirection", world.getLightDirection());
		Vector4f lightInfo = new Vector4f(world.getLightColor(), world.getAmbientStrength());
		shader.loadVector4f("lightInfo", lightInfo);

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		TextureManager.bindBlockDiffuseMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		TextureManager.bindBlockSpecularMap();
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		TextureManager.bindBlockNormalMap();
	}

	public void renderBlocks(WorldClient world){

		for(ClusterPosition pos : world.getClusterRenderPositions()){

			Matrix4f modelMatrix = MathHelper.createTransformationMatrix(pos.getWorldCoords(0, 0, 0).toVector(), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f()));

			ClusterRenderData crd = world.getClusterRenderData(pos);
			StreamModel model = crd.getModel();

			model.bindModel();
			shader.enableAttribArrays();
			shader.loadMatrix("modelMatrix", modelMatrix);

			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getSize(), GL11.GL_UNSIGNED_INT, 0);

			shader.disableAttribArrays();
			model.unbindModel();
		}

		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GLHelper.enableAlphaBlending();
		
		for(ClusterPosition pos : world.getClusterRenderPositions()){

			Matrix4f modelMatrix = MathHelper.createTransformationMatrix(pos.getWorldCoords(0, 0, 0).toVector(), 1.0F, 1.0F, 1.0F, Quaternion.fromVector(new Vector3f()));

			ClusterRenderData crd = world.getClusterRenderData(pos);
			StreamModel model = crd.getTransparentModel();

			model.bindModel();
			shader.enableAttribArrays();
			shader.loadMatrix("modelMatrix", modelMatrix);

			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getSize(), GL11.GL_UNSIGNED_INT, 0);

			shader.disableAttribArrays();
			model.unbindModel();
		}
		
		GLHelper.disableBlending();
	}

	public void end(){
		shader.stop();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}
}
