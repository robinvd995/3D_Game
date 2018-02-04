package converter.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import converter.api.model.Face;
import converter.api.model.IndexedModel;
import converter.api.model.IndexedVertex;
import converter.api.model.ModelSection;
import converter.api.model.Vertex;

public class ModelBuilder {
	
	private int vertexCount;
	private int indicesCount;
	
	private List<IndexedVertex> vertices;
	private HashMap<String,List<Face>> faces;
	
	private Face currentFace;
	
	public ModelBuilder(){
		vertices = new ArrayList<IndexedVertex>();
		faces = new HashMap<String,List<Face>>();
		currentFace = new Face();
		System.out.println(currentFace);
	}
	
	public void createSection(String section){
		faces.put(section, new ArrayList<Face>());
	}
	
	public void addIndex(String sectionId, int index){
		currentFace.setNext(index);
		if(currentFace.isValid()){
			faces.get(sectionId).add(currentFace.copy());
			currentFace = new Face();
		}
	}
	
	public void addVertex(IndexedVertex vertex){
		vertices.add(vertex);
	}
	
	public List<Integer> getIndices(){
		List<Integer> indices = new ArrayList<Integer>();
		for(String s : getAllSections()){
			for(Face face : getFaces(s)){
				indices.add(face.getVert0());
				indices.add(face.getVert1());
				indices.add(face.getVert2());
			}
		}
		return indices;
	}
	
	public List<IndexedVertex> getVertices(){
		return vertices;
	}
	
	public Set<String> getAllSections(){
		return faces.keySet();
	}
	
	public List<Face> getFaces(String section){
		return faces.get(section);
	}
	
	public IndexedModel buildModel(){
		IndexedModel model = new IndexedModel();
		
		for(IndexedVertex vert : vertices){
			model.addVertex(vert);
		}
		
		for(Integer index : getIndices()){
			model.addIndex(index.intValue());
		}
		
		int currentIndex = 0;
		for(String s : getAllSections()){
			List<Face> list = faces.get(s);
			int size = list.size() * 3;
			model.createSection(s, currentIndex, size);
			currentIndex += size;
		}
		
		return model;
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
	
	public static ModelBuilder buildFromObj(RawModel rawModel){
		ModelBuilder builder = new ModelBuilder();
		
		builder.createSection("obj");
		int vertexIndex = 0;
		for(RawFace face : rawModel.getFaces()){
			for(Vertex vertex : face.getVertices()){
				if(!builder.getVertices().contains(vertex)){
					IndexedVertex iv = new IndexedVertex(vertexIndex, vertex);
					builder.addVertex(iv);
					builder.addIndex("obj", vertexIndex);
					vertexIndex++;
					System.out.println("Added a original vertex!");
				}
				else{
					boolean found = false;
					for(IndexedVertex iv : builder.getVertices()){
						if(iv.equals(vertex)){
							builder.addIndex("obj", iv.id);
							System.out.println("Duplicate found!");
							found = true;
							break;
						}
					}
					if(!found){
						System.out.println("Error whilst loading in a duplicate vertex!");
					}
				}
			}
		}
		builder.setVertexCount(vertexIndex);
		builder.setIndicesCount(rawModel.getVertexCount());
		
		return builder;
	}
	
	public static ModelBuilder buildFromIndexedModel(IndexedModel model){
		ModelBuilder builder = new ModelBuilder();
		
		for(IndexedVertex vertex : model.getVertices()){
			builder.addVertex(vertex);
		}
		
		for(String s : model.getAllSections()){
			ModelSection section = model.getSection(s);
			builder.createSection(s);
			for(int i = 0; i < section.getCount(); i++){
				int index = i + section.getStart();
				builder.addIndex(s, model.getIndices().get(index));
			}
		}
		
		return builder;
	}
}
