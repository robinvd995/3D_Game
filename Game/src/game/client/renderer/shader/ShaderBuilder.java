package game.client.renderer.shader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import game.common.json.JsonLoader;

public class ShaderBuilder {

	private List<String> uniformValues = new ArrayList<String>();
	private List<String> attributeValues = new ArrayList<String>();

	private final String versionString;

	private String vertexFile;
	private String fragmentFile;

	private ShaderBuilder(String version) {
		this.versionString = "#version " + version;
	}

	public Shader buildShader(){
		LinkedList<String> vertexSource = loadShaderSource(vertexFile);
		LinkedList<String> fragmentSource = loadShaderSource(fragmentFile);
		
		int vertShaderId = processVertexSource(vertexSource);
		int fragShaderId = processFragmentSource(fragmentSource);
		
		int shaderId = GL20.glCreateProgram();
		GL20.glAttachShader(shaderId, vertShaderId);
		GL20.glAttachShader(shaderId, fragShaderId);
		
		Shader shader = new Shader(shaderId, vertShaderId, fragShaderId);
		
		bindAttributes(shader);
		GL20.glLinkProgram(shaderId);
		GL20.glValidateProgram(shaderId);
		bindUniforms(shader);
		return shader;
	}
	
	private void bindAttributes(Shader shader){
		int curAttrib = 0;
		for(String attrib : attributeValues){
			shader.bindAttribute(curAttrib, attrib);
			curAttrib++;
		}
	}
	
	private void bindUniforms(Shader shader){
		for(String uniform : uniformValues){
			shader.bindUniformLocation(uniform);
		}
	}

	private int processVertexSource(LinkedList<String> source){
		StringBuilder builder = new StringBuilder();
		for(String line : source){
			scanForUniformVariables(line);
			scanForAttributeVariables(line);
			builder.append(line).append("\n");
		}
		return compileShader(GL20.GL_VERTEX_SHADER, builder);
	}

	private int processFragmentSource(LinkedList<String> source){
		StringBuilder builder = new StringBuilder();
		for(String line : source){
			scanForUniformVariables(line);
			builder.append(line).append("\n");
		}
		return compileShader(GL20.GL_FRAGMENT_SHADER, builder);
	}
	
	private int compileShader(int shaderType, StringBuilder builder){
		int shaderId = 0;
		try{
			shaderId = loadShader(builder, shaderType);
		}
		catch(ShaderCompileException e){
			e.printStackTrace();
		}
		return shaderId;
	}
	
	private int loadShader(StringBuilder source, int type) throws ShaderCompileException{
		int shaderId = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderId, source);
		GL20.glCompileShader(shaderId);
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.err.println(GL20.glGetShaderInfoLog(shaderId, 500));
			throw new ShaderCompileException("Failed to compile shader!");
		}
		return shaderId;
	}

	private void scanForUniformVariables(String line){
		if(line.startsWith("uniform")){
			String[] lineSegments = line.split(" ");
			String uniformName = lineSegments[2].replaceAll(";", "");
			uniformValues.add(uniformName);
		}
	}

	private void scanForAttributeVariables(String line){
		if(line.startsWith("in")){
			String[] lineSegments = line.split(" ");
			String attribName = lineSegments[2].replaceAll(";", "");
			attributeValues.add(attribName);
		}
	}
	
	private LinkedList<String> loadShaderSource(String file){
		LinkedList<String> source = loadShaderSource(file, new LinkedList<String>());
		if(source != null && !source.isEmpty()){
			source.addFirst(versionString);
		}
		return source;
	}

	private LinkedList<String> loadShaderSource(String file, List<String> includedFiles){
		LinkedList<String> list = new LinkedList<String>();
		if(includedFiles.contains(file)){
			return new LinkedList<String>();
		}
		includedFiles.add(file);
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file + ".glsl"));
			String line;
			while((line = reader.readLine()) != null){
				if(line.isEmpty()) continue;
				if(line.startsWith("#include")){
					String includedFile = line.split(" ")[1].trim();
					LinkedList<String> includedSource = loadShaderSource(includedFile.replace(".", "/"), includedFiles);
					list.addAll(includedSource);
					continue;
				}
				list.add(line);
			}
			reader.close();
		}
		catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		return list;
	}

	public static ShaderBuilder newInstance(String shader) {
		ShaderMeta meta = null;
		try {
			meta = JsonLoader.loadClassFromJson("shaders/meta/" + shader, ShaderMeta.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ShaderBuilder builder = new ShaderBuilder(meta.getVersion());
		builder.vertexFile = meta.getVertexFile();
		builder.fragmentFile = meta.getFragmentFile();
		return builder;
	}
}
