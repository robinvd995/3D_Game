package game.common.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class JsonLoader {

	private static Gson gsonInstance = new Gson();
	
	public static <T> T loadClassFromJson (String filePath, Class<T> clzz) throws FileNotFoundException, IOException{
		File file = new File(filePath + ".json");
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(file));

		String jsonData = "";
		if(reader != null){
			String line = "";
			while((line = reader.readLine()) != null){
				jsonData = jsonData + line.trim();
			}
		}
		
		reader.close();

		T data = gsonInstance.fromJson(jsonData, clzz);
		return data;
	}

	public static void saveJsonFile(String path, String fileName, Object src) throws IOException{
		File filePath = new File(path);
		if(!filePath.exists()){
			filePath.mkdirs();
		}
		File file = new File(path + "/" + fileName + ".json");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fwriter = new FileWriter(file);
		gsonInstance.toJson(src, fwriter);
		fwriter.flush();
	}
}
