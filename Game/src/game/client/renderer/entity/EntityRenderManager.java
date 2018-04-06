package game.client.renderer.entity;

import java.util.HashMap;

import org.lwjgl.opengl.GL30;

import caesar.util.Matrix4f;
import game.client.display.DisplayManager;
import game.client.renderer.shader.Shader;
import game.client.renderer.shader.ShaderBuilder;
import game.client.renderer.world.WorldClient;
import game.common.entity.Entity;
import game.common.entity.EntityPalmTree;
import game.common.entity.Player;

public class EntityRenderManager {

	private HashMap<Class<? extends Entity>,EntityRenderer> entityRendererMap = new HashMap<Class<? extends Entity>,EntityRenderer>();

	private Shader shader;

	public EntityRenderManager(){
		shader = ShaderBuilder.buildShader("entity");
	}

	public void initRenderer(){
		this.registerEntityRenderer(Player.class, new PlayerRenderer());
		this.registerEntityRenderer(EntityPalmTree.class, new PalmTreeRenderer());
	}

	public void prepare(WorldClient world, Matrix4f viewMatrix) {
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();

		shader.start();
		shader.loadMatrix("viewMatrix", viewMatrix);
		shader.loadMatrix("projectionMatrix", projectionMatrix);
		shader.loadVector3f("lightDirection", world.getLightDirection());
	}

	public void renderEntities(WorldClient world, double delta){
		
		for(Class<? extends Entity> clzz : world.getEntityClassesToRender()){
			EntityRenderer renderer = entityRendererMap.get(clzz);
			if(renderer == null)
				throw new RuntimeException("No entity renderer found for " + clzz.toString());
			
			renderer.bindModel();
			shader.enableAttribArrays();
			
			for(Entity entity : world.getEntityInstancesForClass(clzz)){
				Matrix4f modelMatrix = entity.getTransform().getTransformationMatrix();
				if(renderer.preloadTransformation())
					shader.loadMatrix("modelMatrix", modelMatrix);
				renderer.renderEntity(shader, entity, delta);
			}
			
			shader.disableAttribArrays();
		}
	}
	
	public void end(){

		GL30.glBindVertexArray(0);

		shader.stop();
	}

	private void registerEntityRenderer(Class<? extends Entity> clzz, EntityRenderer entityRenderer){
		if(entityRendererMap.containsKey(clzz)){
			throw new IllegalArgumentException("There already is a renderer mapped for class " + clzz.getName());
		}
		else{
			entityRendererMap.put(clzz, entityRenderer);
		}
	}
}
