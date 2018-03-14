package game.client.renderer.tessellation;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.BufferUtils;

import caesar.util.Vector2f;
import caesar.util.Vector3f;
import game.common.util.EnumDirection;

public class Tessellator {

	public static final int MODE_TRIANGLES = 0;
	public static final int MODE_QUADS = 1;
	public static final int MODE_TRIANGLED_QUAD = 2;

	private List<Float> vertices;
	private List<Float> uvs;
	private List<Float> normals;
	private List<Integer> indices;

	private List<Float> tangents;
	
	private int currentPosition = 0;

	private List<Vertex> currentVertices;

	private final int mode;

	private float tessellateOffsetX = 0.0f;
	private float tessellateOffsetY = 0.0f;
	private float tessellateOffsetZ = 0.0f;
	
	private float normalX;
	private float normalY;
	private float normalZ;
	
	public Tessellator(int mode){
		this.mode = mode;

		this.vertices = new LinkedList<Float>();
		this.uvs = new LinkedList<Float>();
		this.normals = new LinkedList<Float>();
		this.indices = new LinkedList<Integer>();
		
		this.tangents = new LinkedList<Float>();

		this.currentVertices = new ArrayList<Vertex>();
	}
	
	public void addVertex(float x, float y, float z, float u, float v){
		addVertex(x, y, z, u, v, normalX, normalY, normalZ);
	}

	public void addVertex(float px, float py, float pz, float u, float v, float nx, float ny, float nz){
		currentVertices.add(new Vertex(px + tessellateOffsetX, py + tessellateOffsetY, pz + tessellateOffsetZ, u, v, nx, ny, nz));
	}

	public void tessellate(){
		switch(mode){
		case MODE_TRIANGLES: tessellateTriangle(); break;
		case MODE_QUADS: tessellateQuad(); break;
		case MODE_TRIANGLED_QUAD: tesselateTriangledQuad(); break;
		default: throw new TessellationException("Undefined tessellation mode selected!");
		}
		currentVertices.clear();
	}

	private void tesselateTriangledQuad(){
		if(currentVertices.size() != 4) throw new TessellationException("An incorrect amount of vertices have been queued to create a triangled quad!");
		
		Vertex v0 = currentVertices.get(0);
		Vertex v1 = currentVertices.get(1);
		Vertex v2 = currentVertices.get(2);
		Vertex v3 = currentVertices.get(3);
		
		//Tangent for vertex 0
		Vector3f tangent0 = calculateTangent(v0, v1, v2);
		//Tangent for vertex 3
		Vector3f tangent1 = calculateTangent(v0, v2, v3);
		//Tangent for vertex 1,2
		Vector3f tangent2 = Vector3f.add(tangent0, tangent1);
		
		addVertex(currentVertices.get(0), tangent0);
		addVertex(currentVertices.get(1), tangent2);
		addVertex(currentVertices.get(2), tangent2);
		addVertex(currentVertices.get(3), tangent1);
		
		indices.add(currentPosition + 0);
		indices.add(currentPosition + 1);
		indices.add(currentPosition + 2);
		indices.add(currentPosition + 0);
		indices.add(currentPosition + 2);
		indices.add(currentPosition + 3);
		
		currentPosition += 4;
	}
	
	private void tessellateTriangle(){
		throw new TessellationException("Method not yet implemented!");
	}

	private void tessellateQuad(){
		throw new TessellationException("Method not yet implemented!");
	}
	
	private Vector3f calculateTangent(Vertex v0, Vertex v1, Vertex v2){
		Vector3f pos0 = new Vector3f(v0.getPosX(), v0.getPosY(), v0.getPosZ());
		Vector3f pos1 = new Vector3f(v1.getPosX(), v1.getPosY(), v1.getPosZ());
		Vector3f pos2 = new Vector3f(v2.getPosX(), v2.getPosY(), v2.getPosZ());
		
		Vector2f uv0 = new Vector2f(v0.getU(), v0.getV());
		Vector2f uv1 = new Vector2f(v1.getU(), v1.getV());
		Vector2f uv2 = new Vector2f(v2.getU(), v2.getV());
		
		Vector3f deltaPos1 = Vector3f.sub(pos1, pos0);
		Vector3f deltaPos2 = Vector3f.sub(pos2, pos0);
		
		Vector2f deltaUV1 = Vector2f.sub(uv1, uv0);
		Vector2f deltaUV2 = Vector2f.sub(uv2, uv0);
		
		float r = 1.0f / (deltaUV1.getX() * deltaUV2.getY() - deltaUV1.getY() * deltaUV2.getX());
		Vector3f t0 = Vector3f.mult(deltaPos1, deltaUV2.getY());
		Vector3f t1 = Vector3f.mult(deltaPos2, deltaUV1.getY());
		Vector3f t2 = Vector3f.sub(t0, t1);
		Vector3f tangent = Vector3f.mult(t2, r);
		return tangent;
	}
	
	private void addVertex(Vertex vert){
		vertices.add(vert.getPosX());
		vertices.add(vert.getPosY());
		vertices.add(vert.getPosZ());
		uvs.add(vert.getU());
		uvs.add(vert.getV());
		normals.add(vert.getNormX());
		normals.add(vert.getNormY());
		normals.add(vert.getNormZ());
	}
	
	private void addVertex(Vertex vert, Vector3f tangent){
		vertices.add(vert.getPosX());
		vertices.add(vert.getPosY());
		vertices.add(vert.getPosZ());
		uvs.add(vert.getU());
		uvs.add(vert.getV());
		normals.add(vert.getNormX());
		normals.add(vert.getNormY());
		normals.add(vert.getNormZ());
		tangents.add(tangent.getX());
		tangents.add(tangent.getY());
		tangents.add(tangent.getZ());
		
		//System.out.println("Tangent: " + tangent);
	}
	
	public FloatBuffer getVertexBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.size());
		for(float f : vertices) buffer.put(f);
		buffer.flip();
		return buffer;
	}
	
	public FloatBuffer getUvBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(uvs.size());
		for(float f : uvs) buffer.put(f);
		buffer.flip();
		return buffer;
	}
	
	public FloatBuffer getNormalBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(normals.size());
		for(float f : normals) buffer.put(f);
		buffer.flip();
		return buffer;
	}
	
	public IntBuffer getIndexBuffer(){
		IntBuffer buffer = BufferUtils.createIntBuffer(indices.size());
		for(int i : indices) buffer.put(i);
		buffer.flip();
		return buffer;
	}
	
	public FloatBuffer getTangentBuffer(){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(tangents.size());
		for(float f : tangents) buffer.put(f);
		buffer.flip();
		return buffer;
	}
	
	/*public int[] getIndices(){
		int[] ia = new int[indices.size()];
		int c = 0;
		for(int i : indices){
			ia[c] = i;
			c++;
		}
		return ia;
	}
	
	public float[] getVertices(){
		float[] va = new float[vertices.size()];
		for(int i = 0; i < va.length; i++){
			va[i] = vertices.get(i);
		}
		return va;
	}
	
	public float[] getNormals(){
		float[] va = new float[uvs.size()];
		for(int i = 0; i < va.length; i++){
			va[i] = uvs.get(i);
		}
		return va;
	}
	
	public float[] getUvs(){
		float[] va = new float[normals.size()];
		for(int i = 0; i < va.length; i++){
			va[i] = normals.get(i);
		}
		return va;
	}*/
	
	public void setTessellateOffset(float offX, float offY, float offZ){
		this.tessellateOffsetX = offX;
		this.tessellateOffsetY = offY;
		this.tessellateOffsetZ = offZ;
	}
	
	public void setNormal(float normX, float normY, float normZ){
		this.normalX = normX;
		this.normalY = normY;
		this.normalZ = normZ;
	}
	
	public void setNormal(EnumDirection dir){
		this.setNormal(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ());
	}
}
