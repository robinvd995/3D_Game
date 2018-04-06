package game.client.renderer.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

@Deprecated
public class ShaderBuilderOld {
	
	private static List<String> uniformValues = new ArrayList<String>();
	private static List<String> attributeValues = new ArrayList<String>();
	
	public static Shader buildShader(String shaderName){
		String file = "res/shaders/" + shaderName;
		uniformValues.clear();
		attributeValues.clear();
		
		int vertexShader = loadVertexShader(file);
		int fragmentShader = loadFragmentShader(file);
		int shaderId = GL20.glCreateProgram();
		GL20.glAttachShader(shaderId, vertexShader);
		GL20.glAttachShader(shaderId, fragmentShader);
		
		Shader shader = new Shader(shaderId, vertexShader, fragmentShader);
		
		bindAttributes(shader);
		GL20.glLinkProgram(shaderId);
		GL20.glValidateProgram(shaderId);
		bindUniforms(shader);
		return shader;
	}
	
	private static void bindAttributes(Shader shader){
		int curAttrib = 0;
		for(String attrib : attributeValues){
			shader.bindAttribute(curAttrib, attrib);
			curAttrib++;
		}
	}
	
	private static void bindUniforms(Shader shader){
		for(String uniform : uniformValues){
			shader.bindUniformLocation(uniform);
		}
	}
	
	/*private List<String> readShaderFile(String file){
		List<String> list = new ArrayList<String>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file + ".frag"));
			String line;
			while((line = reader.readLine()) != null){
				if(line.startsWith("#include")){
					String includedFile = line.split(" ")[1].trim();
					if(includedFiles.contains(includedFile)){
						continue;
					}
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
	}*/
	
	private static int loadFragmentShader(String file){
		StringBuilder builder = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file + ".frag"));
			String line;
			while((line = reader.readLine()) != null){
				
				if(line.startsWith("uniform")){
					String[] lineSegments = line.split(" ");
					String uniformName = lineSegments[2].replaceAll(";", "");
					uniformValues.add(uniformName);
				}
				
				builder.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderId = 0;
		try{
			shaderId = compileShader(builder, GL20.GL_FRAGMENT_SHADER);
		}
		catch(ShaderCompileException e){
			System.err.println("Could not compile shader, " + file + ".frag");
		}
		return shaderId;
	}
	
	private static int loadVertexShader(String file){
		StringBuilder builder = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file + ".vert"));
			String line;
			while((line = reader.readLine()) != null){
				if(line.startsWith("in")){
					String[] lineSegments = line.split(" ");
					String attribName = lineSegments[2].replaceAll(";", "");
					attributeValues.add(attribName);
				}
				else if(line.startsWith("uniform")){
					String[] lineSegments = line.split(" ");
					String uniformName = lineSegments[2].replaceAll(";", "");
					uniformValues.add(uniformName);
				}
				builder.append(line).append("\n");
			}
			reader.close();
		}
		catch(IOException e){
			System.err.println("Could not read file!");
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderId = 0;
		try{
			shaderId = compileShader(builder, GL20.GL_VERTEX_SHADER);
		}
		catch(ShaderCompileException e){
			System.err.println("Could not compile shader, " + file + ".vert");
		}
		return shaderId;
	}
	
	private static int compileShader(StringBuilder source, int type) throws ShaderCompileException{
		int shaderId = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderId, source);
		GL20.glCompileShader(shaderId);
		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
			System.err.println(GL20.glGetShaderInfoLog(shaderId, 500));
			throw new ShaderCompileException();
		}
		return shaderId;
	}
	
	public static class ShaderCompileException extends Exception{
		private static final long serialVersionUID = 3685083155054162315L;}
}
