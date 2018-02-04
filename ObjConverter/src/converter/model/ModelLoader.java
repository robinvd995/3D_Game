package converter.model;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import converter.api.model.IndexedModel;
import converter.api.model.IndexedVertex;
import converter.api.model.ModelSection;

public class ModelLoader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();

	public LoadedModel loadToVAO(IndexedModel model){
		int vaoID = createVAO();

		LoadedModel loadedModel = new LoadedModel(vaoID);
		
		List<Integer> indicesList = model.getIndices();
		int i = 0;
		int[] indices = new int[indicesList.size()];
		for(i = 0; i < indices.length; i++){
			indices[i] = indicesList.get(i);
		}

		List<IndexedVertex> vertices = model.getVertices();
		float[] positions = new float[vertices.size() * 3];
		float[] textureCoords = new float[vertices.size() * 2];
		float[] normals = new float[vertices.size() * 3];

		i = 0;
		for(IndexedVertex vertex : vertices){
			int pp = i * 3;
			int tp = i * 2;
			int np = i * 3;
			positions[pp + 0] = vertex.getPosition().getX();
			positions[pp + 1] = vertex.getPosition().getY();
			positions[pp + 2] = vertex.getPosition().getZ();
			textureCoords[tp + 0] = vertex.getUv().getX();
			textureCoords[tp + 1] = vertex.getUv().getY();
			normals[np + 0] = vertex.getNormal().getX();
			normals[np + 1] = vertex.getNormal().getY();
			normals[np + 2] = vertex.getNormal().getZ();
			i++;
		}

		bindIndicesBuffer(indices);
		storeDataInAttribList(0, 3, positions);
		storeDataInAttribList(1, 2, textureCoords);
		storeDataInAttribList(2, 3, normals);
		unbindVAO();
		
		for(String s : model.getAllSections()){
			ModelSection section = model.getSection(s);
			loadedModel.addSection(s, section.getStart(), section.getCount());
		}
		
		return loadedModel;
	}

	public int loadLines(float[] positions){
		int vaoID = createVAO();
		storeDataInAttribList(0, 2, positions);
		unbindVAO();
		return vaoID;
	}

	private int createVAO(){
		int id = GL30.glGenVertexArrays();
		vaos.add(id);
		GL30.glBindVertexArray(id);
		return id;
	}

	private void storeDataInAttribList(int attributeNumer, int coordinteSize, float[] data){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumer, coordinteSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void cleanUp(){
		for(int vao : vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}
}
