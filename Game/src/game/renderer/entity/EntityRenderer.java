package game.renderer.entity;

import java.io.File;

import converter.api.OIMModelLoader;
import converter.api.model.IndexedModel;
import game.entity.Entity;
import game.renderer.model.LoadedModel;
import game.renderer.model.ModelLoader;
import game.renderer.shader.EntityShader;

public abstract class EntityRenderer {

	protected static final String MODEL_FOLDER = "res/models/entity/";
	
	protected LoadedModel model;
	
	public EntityRenderer(String modelName){
		IndexedModel indexedModel = OIMModelLoader.loadModel(new File(MODEL_FOLDER + modelName + ".oim"));
		if(indexedModel != null){
			model = ModelLoader.INSTANCE.loadIndexedModel(indexedModel);
		}
	}
	
	public abstract void renderEntity(EntityShader shader, Entity entity);
	
	public void bindModel(){
		model.bindModel();
	}
}
