package converter.io;

public class ObjConverter {

	/*public static IndexedModel convertObj(RawModel rawModel){
		
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
		return builder.buildModel();
	}*/
}