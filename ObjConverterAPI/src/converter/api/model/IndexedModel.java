package converter.api.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class IndexedModel {

	private int vertexCount;
	private int indicesCount;
	
	private List<IndexedVertex> vertices;
	private List<Integer> indices;
	private HashMap<String,ModelSection> sections;
	
	public IndexedModel(){
		this.vertices = new ArrayList<IndexedVertex>();
		this.indices = new ArrayList<Integer>();
		this.sections = new HashMap<String,ModelSection>();
	}
	
	public void addVertex(IndexedVertex vertex){
		vertices.add(vertex);
	}
	
	public List<IndexedVertex> getVertices(){
		return vertices;
	}
	
	public void addIndex(int index){
		indices.add(index);
	}
	
	public List<Integer> getIndices(){
		return indices;
	}
	
	public void createSection(String section, int start, int count){
		sections.put(section, new ModelSection(start, count));
	}
	
	public ModelSection getSection(String section){
		return sections.get(section);
	}
	
	public Set<String> getAllSections(){
		return sections.keySet();
	}
	
	public void setVertexCount(int count){
		this.vertexCount = count;
	}
	
	public void setIndicesCount(int count){
		this.indicesCount = count;
	}
	
	public int getVertexCount(){
		return vertexCount;
	}
	
	public int getIndicesCount(){
		return indicesCount;
	}
}
