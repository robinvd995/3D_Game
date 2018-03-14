package game.client.renderer.model;

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

	public static final ModelLoader INSTANCE = new ModelLoader();

	private List<LoadedModel> models = new ArrayList<LoadedModel>();

	private ModelLoader(){}

	public LoadedModel loadIndexedModel(IndexedModel model){
		int vaoID = createVAO();

		LoadedModel loadedModel = new LoadedModel(vaoID, GL11.GL_TRIANGLES);

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

		bindIndicesBuffer(loadedModel, indices);
		storeDataInAttribList(loadedModel, 0, 3, positions);
		storeDataInAttribList(loadedModel, 1, 2, textureCoords);
		storeDataInAttribList(loadedModel, 2, 3, normals);
		unbindVAO();

		for(String s : model.getAllSections()){
			ModelSection section = model.getSection(s);
			loadedModel.addSection(s, section.getStart(), section.getCount());
		}

		models.add(loadedModel);
		return loadedModel;
	}

	/*public GuiQuad loadGuiComponentQuad(float[] vertices, float[] uvs, int[] indices){
		int vao = createVAO();
		GuiQuad quad = new GuiQuad(vao);
		quad.addVbo(bindIndicesBuffer(indices));
		quad.addVbo(storeDataInAttribList(0, 2, vertices));
		quad.addVbo(storeDataInAttribList(1, 2, uvs));
		unbindVAO();
		return quad;
	}*/
	
	public SimpleModel load2DIndexedSimpleModel(float[] vertices, float[] uvs, int[] indices){
		int vao = createVAO();
		SimpleModel model = new SimpleModel(vao, indices.length);
		model.addVbo(bindIndicesBuffer(indices));
		model.addVbo(storeDataInAttribList(0, 2, vertices));
		model.addVbo(storeDataInAttribList(1, 2, uvs));
		unbindVAO();
		return model;
	}
	
	public SimpleModel load2DSimpleModel(float[] vertices, float[] uvs){
		int vao = createVAO();
		SimpleModel model = new SimpleModel(vao, vertices.length);
		model.addVbo(storeDataInAttribList(0, 2, vertices));
		model.addVbo(storeDataInAttribList(1, 2, uvs));
		unbindVAO();
		return model;
	}
	
	public SimpleModel loadSimpleCube(float[] vertices){
		int vao = createVAO();
		SimpleModel model = new SimpleModel(vao, 36);
		model.addVbo(storeDataInAttribList(0, 3, vertices));
		return model;
	}
	
	public SimpleModel loadSimpleQuad(float[] vertices){
		int vao = createVAO();
		SimpleModel model = new SimpleModel(vao, vertices.length / 2);
		model.addVbo(storeDataInAttribList(0, 2, vertices));
		return model;
	}
	
	/*public LoadedModel loadAxisAlignedBBAsModel(AxisAlignedBB aabb){
		
		int vao = createVAO();
		LoadedModel model = new LoadedModel(vao, GL11.GL_LINES);
		
		List<Vector3f> points = aabb.getAsPoints();
		float[] positions = new float[points.size() * 3];
		int i = 0;
		for(Vector3f point : points){
			positions[i++] = point.getX();
			positions[i++] = point.getY();
			positions[i++] = point.getZ();
		}
		
		int[] indices = new int[]{
				0,1, 1,2, 2,3, 3,0,
				0,4, 1,5, 2,6, 3,7,
				4,5, 5,6, 6,7, 7,4
		};
		
		bindIndicesBuffer(model, indices);
		storeDataInAttribList(model, 0, 3, positions);
		
		models.add(model);
		return model;
	}*/
	
	public StreamModel createStreamModel(){
		int vao = createVAO();
		StreamModel model = new StreamModel(vao, GL15.GL_STREAM_DRAW);
		unbindVAO();
		return model;
	}
	
	public StreamModel createDynamicModel(){
		int vao = createVAO();
		StreamModel model = new StreamModel(vao, GL15.GL_DYNAMIC_DRAW);
		unbindVAO();
		return model;
	}
	
	/*public int loadLines(float[] positions){
		int vaoID = createVAO();
		storeDataInAttribList(0, 2, positions);
		unbindVAO();
		return vaoID;
	}*/

	private int createVAO(){
		int id = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(id);
		return id;
	}

	private int storeDataInAttribList(int attributeNumer, int coordinateSize, float[] data){
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		return storeDataInAttribList(attributeNumer, coordinateSize, buffer);
	}
	
	private int storeDataInAttribList(int attributeNumer, int coordinateSize, FloatBuffer buffer){
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumer, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}
	
	private void storeDataInAttribList(LoadedModel model, int attributeNumer, int coordinteSize, float[] data){
		int vboID = GL15.glGenBuffers();
		model.addVbo(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumer, coordinteSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO(){
		GL30.glBindVertexArray(0);
	}

	private int bindIndicesBuffer(int[] indices){
		IntBuffer buffer = storeDataInIntBuffer(indices);
		return bindIndicesBuffer(buffer);
	}
	
	private int bindIndicesBuffer(IntBuffer buffer){
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		return vbo;
	}
	
	private void bindIndicesBuffer(LoadedModel model, int[] indices){
		int vboID = GL15.glGenBuffers();
		model.addVbo(vboID);
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

	public FloatBuffer storeDataInFloatBuffer(float[] data){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void cleanUp(){
		for(LoadedModel model : models){
			GL30.glDeleteVertexArrays(model.getVao());
			for(int vbo : model.getVbos()){
				GL15.glDeleteBuffers(vbo);
			}
		}
	}
}

