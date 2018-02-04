package converter.model;

import java.util.HashMap;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import converter.api.model.ModelSection;

public class LoadedModel {

	private int vao;

	private HashMap<String,ModelSection> sectionMap;

	public LoadedModel(int vao){
		this.vao = vao;
		this.sectionMap = new HashMap<String,ModelSection>();
	}

	public int getVao(){
		return vao;
	}

	public void renderAll(){
		for(ModelSection section : sectionMap.values()){
			GL11.glDrawElements(GL11.GL_TRIANGLES, section.getCount(), GL11.GL_UNSIGNED_INT, section.getStart() * 4);
		}
	}

	public void renderSection(String sectionId){
		ModelSection section = sectionMap.get(sectionId);
		GL11.glDrawElements(GL11.GL_TRIANGLES, section.getCount(), GL11.GL_UNSIGNED_INT, section.getStart() * 4);
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
