package game.client.renderer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import converter.api.model.ModelSection;

public class LoadedModel extends ModelBase{

	private final int drawMode;
	
	private List<Integer> vbos = new ArrayList<Integer>();
	private HashMap<String,ModelSection> sectionMap;
	
	public LoadedModel(int vao, int drawMode){
		super(vao);
		this.drawMode = drawMode;
		this.sectionMap = new HashMap<String,ModelSection>();
	}
	
	public void addVbo(int vbo){
		vbos.add(vbo);
	}
	
	public List<Integer> getVbos(){
		return vbos;
	}

	public void renderAll(){
		for(ModelSection section : sectionMap.values()){
			//GL11.glDrawElements(mode, section.getCount(), GL11.GL_UNSIGNED_INT, section.getStart() * 4);
			renderSection(section);
		}
	}

	public void renderSection(String sectionId){
		ModelSection section = sectionMap.get(sectionId);
		renderSection(section);
		//GL11.glDrawElements(mode, section.getCount(), GL11.GL_UNSIGNED_INT, section.getStart() * 4);
	}

	private void renderSection(ModelSection section){
		GL11.glDrawElements(drawMode, section.getCount(), GL11.GL_UNSIGNED_INT, section.getStart() * 4);
	}
	
	public void addSection(String section, int start, int count){
		this.sectionMap.put(section, new ModelSection(start, count));
	}

	public Set<String> getAllSections(){
		return sectionMap.keySet();
	}

	public int getSectionStart(String section){
		return sectionMap.get(section).getStart();
	}

	public int getSectionCount(String section){
		return sectionMap.get(section).getCount();
	}
}
