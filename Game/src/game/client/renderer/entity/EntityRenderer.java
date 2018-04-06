package game.client.renderer.entity;

import java.io.File;

import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import game.client.renderer.model.LoadedModel;
import game.client.renderer.model.ModelLoader;
import game.client.renderer.shader.Shader;
import game.common.entity.Entity;

public abstract class EntityRenderer {

	protected static final String MODEL_FOLDER = "res/models/entity/";
	
	protected LoadedModel model;
	
	public EntityRenderer(){}
	
	public EntityRenderer(String modelName){
		IndexedModel indexedModel = OIMModelLoader.loadModel(new File(MODEL_FOLDER + modelName + ".oim"));
		if(indexedModel != null){
			model = ModelLoader.INSTANCE.loadIndexedModel(indexedModel);
		}
	}
	
	public abstract void renderEntity(Shader shader, Entity entity, double delta);
	
	public void bindModel(){
		model.bindModel();
	}
	
	public boolean preloadTransformation(){
		return true;
	}
}
