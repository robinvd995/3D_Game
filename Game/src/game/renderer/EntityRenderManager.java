package game.renderer;

import java.util.HashMap;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.display.DisplayManager;
import game.entity.Entity;
import game.entity.Player;
import game.renderer.entity.EntityRenderer;
import game.renderer.entity.PlayerRenderer;
import game.renderer.shader.EntityShader;
import game.world.World;

public class EntityRenderManager {

	private HashMap<Class<? extends Entity>,EntityRenderer> entityRendererMap = new HashMap<Class<? extends Entity>,EntityRenderer>();

	private EntityShader shader;
	//private LoadedModel model;

	public EntityRenderManager(){
		shader = new EntityShader();
		/*IndexedModel indexedModel = OIMModelLoader.loadModel(new File("res/models/block.oim"));
		model = ModelLoader.INSTANCE.loadIndexedModel(indexedModel);*/
	}

	public void initRenderer(){
		this.registerEntityRenderer(Player.class, new PlayerRenderer());
	}

	public void prepare(Vector3f lightDir, Matrix4f viewMatrix) {
		Matrix4f projectionMatrix = DisplayManager.INSTANCE.getProjectionMatrix();

		shader.start();
		shader.loadViewMatrix(viewMatrix);
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLightDirection(lightDir);
	}

	public void renderEntity(World world, Entity entity){

		//int vao = model.getVao();
		//GL30.glBindVertexArray(vao);
		
		EntityRenderer entityRenderer = entityRendererMap.get(entity.getClass());
		entityRenderer.bindModel();

		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);

		Matrix4f modelMatrix = entity.getTransform().getTransformationMatrix();
		shader.loadModelMatrix(modelMatrix);

		entityRenderer.renderEntity(shader, entity);
	}

	public void end(){
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);

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
