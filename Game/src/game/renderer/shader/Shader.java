package game.renderer.shader;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import caesar.util.Matrix4f;
import caesar.util.Vector2f;
import caesar.util.Vector3f;
import caesar.util.Vector4f;

public class Shader {

	private final int shaderId;
	private final int vertexId;
	private final int fragmentId;
	
	private HashMap<String,Integer> uniformMap = new HashMap<String,Integer>();
	
	private List<Integer> attributes = new ArrayList<Integer>();
	
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public Shader(int shaderId, int vertexId, int fragmentId){
		this.shaderId = shaderId;
		this.vertexId = vertexId;
		this.fragmentId = fragmentId;
	}
	
	public void start(){
		GL20.glUseProgram(shaderId);
	}
	
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	public void delete(){
		stop();
		GL20.glDetachShader(shaderId, vertexId);
		GL20.glDetachShader(shaderId, fragmentId);
		GL20.glDeleteShader(vertexId);
		GL20.glDeleteShader(fragmentId);
		GL20.glDeleteProgram(shaderId);
	}
	
	public void enableAttribArrays(){
		for(Integer i : attributes){
			GL20.glEnableVertexAttribArray(i);
		}
	}
	
	public void disableAttribArrays(){
		for(Integer i : attributes){
			GL20.glDisableVertexAttribArray(i);
		}
	}
	
	protected void bindAttribute(int attributeNumber, String name){
		GL20.glBindAttribLocation(shaderId, attributeNumber, name);
		attributes.add(attributeNumber);
		System.out.println("Bind attribute: " + name + " in slot " + attributeNumber);
	}
	
	protected void bindUniformLocation(String uniformName){
		int uniformLocation = GL20.glGetUniformLocation(shaderId, uniformName);
		uniformMap.put(uniformName, uniformLocation);
		System.out.println("Loading uniform " + uniformName);
	}
	
	public void loadFloat(String uniform, float value){
		int location = uniformMap.get(uniform);
		GL20.glUniform1f(location, value);
	}
	
	public void loadVector2f(String uniform, Vector2f value){
		int location = uniformMap.get(uniform);
		GL20.glUniform2f(location, value.getX(), value.getY());
	}
	
	public void loadVector3f(String uniform, Vector3f value){
		int location = uniformMap.get(uniform);
		GL20.glUniform3f(location, value.getX(), value.getY(), value.getZ());
	}
	
	public void loadVector4f(String uniform, Vector4f value){
		int location = uniformMap.get(uniform);
		GL20.glUniform4f(location, value.getX(), value.getY(), value.getZ(), value.getW());
	}
	
	public void loadMatrix(String uniform, Matrix4f value){
		int location = uniformMap.get(uniform);
		value.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4fv(location, false, matrixBuffer);
	}
}
