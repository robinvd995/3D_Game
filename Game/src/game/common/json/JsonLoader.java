package game.common.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

public class JsonLoader {

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

		Gson gson = new Gson();
		T data = gson.fromJson(jsonData, clzz);
		System.out.println(data);
		return data;
	}

}
