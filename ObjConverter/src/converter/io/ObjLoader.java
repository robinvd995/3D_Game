package converter.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import caesar.util.Vector2f;
import caesar.util.Vector3f;
import converter.api.model.Vertex;
import converter.model.RawFace;
import converter.model.RawModel;

public class ObjLoader {

	public static final String POSITION_PREFIX = "v ";
	public static final String UV_PREFIX = "vt";
	public static final String NORMAL_PREFIX = "vn";
	public static final String FACE_PREFIX = "f ";
	
	public static RawModel loadOBJModel(File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		RawModel model = new RawModel();
		
		List<Vector3f> positions = new LinkedList<Vector3f>();
		List<Vector2f> uvs = new LinkedList<Vector2f>();
		List<Vector3f> normals = new LinkedList<Vector3f>();
		
		String line;
		while((line = reader.readLine()) != null){
			if(line.length() < 2) continue;
			String prefix = line.substring(0, 2);
			switch(prefix){
			default: continue;
			case POSITION_PREFIX:
				positions.add(Vector3f.from(getArgumentsFromString(3, line.substring(2), " ")));
				break;
			
			case UV_PREFIX:
				uvs.add(Vector2f.from(getArgumentsFromString(2, line.substring(2), " ")));
				break;
				
			case NORMAL_PREFIX:
				normals.add(Vector3f.from(getArgumentsFromString(3, line.substring(2), " ")));
				break;
				
			case FACE_PREFIX:
				String[] args = getArgumentsFromString(3, line.substring(2), " ");
				RawFace face = new RawFace(3);
				for(int i = 0; i < 3; i++){
					String[] vertexData = getArgumentsFromString(3, args[i], "/");
					Vector3f vertexPosition = positions.get(Integer.parseInt(vertexData[0]) - 1);
					Vector2f vertexUV = uvs.get(Integer.parseInt(vertexData[1]) - 1);
					Vector3f vertexNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
					face.addVertex(new Vertex(vertexPosition, vertexUV, vertexNormal));
				}
				model.addFace(face);
				break;
			}
		}
		
		reader.close();
		return model;
	}
	
	public static String[] getArgumentsFromString(int count, String line, String delimiter){
		
		String[] args = new String[count];
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(delimiter);
		
		for(int i = 0; i < count; i++){
			args[i] = scanner.next();
		}
		
		scanner.close();
		
		return args;
	}
}
