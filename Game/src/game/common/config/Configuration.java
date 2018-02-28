package game.common.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class Configuration {

	private final String filePath;

	private HashMap<String,CategoryContainer> categoryMap = new HashMap<String,CategoryContainer>();
	
	private Configuration(String filePath){
		this.filePath = filePath;
	}

	private void createNewCategory(String category){
		categoryMap.put(category, new CategoryContainer());
	}

	private void add(String cat, String name, EnumConfigType type, String value){
		categoryMap.get(cat).add(name, type, value);
	}
	
	public void setValue(String category, String name, String value) throws ConfigurationException{
		categoryMap.get(category).set(name, value);
	}

	public int getInteger(String cat, String name) throws ConfigurationException{
		return categoryMap.get(cat).getInteger(name);
	}
	
	public boolean getBoolean(String cat, String name) throws ConfigurationException{
		return categoryMap.get(cat).getBoolean(name);
	}
	
	public float getFloat(String cat, String name) throws ConfigurationException{
		return categoryMap.get(cat).getFloat(name);
	}
	
	public String getString(String cat, String name) throws ConfigurationException{
		return categoryMap.get(cat).getString(name);
	}
	
	public void saveConfig(){
		File file = new File(filePath);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(writer == null)
			return;
		
		Set<String> categories = categoryMap.keySet();
		for(String s : categories){
			writeLine(writer, s + "{");
			CategoryContainer container = categoryMap.get(s);
			for(ConfigEntry entry : container.getEntries()){
				String line = "    " + entry.toString();
				writeLine(writer, line);
			}
			writeLine(writer, "}");
		}
		
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Configuration loadConfig(String filePath) throws ConfigurationException{

		String file = filePath + ".cfg";
		File cfgFile = new File(file);

		Configuration config = new Configuration(file);

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(cfgFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if(reader == null){
			return config;
		}

		String currentCategory = null;
		String line;
		while((line = readNextLine(reader)) != null){
			line = line.trim();
			
			if(line.endsWith("{")){

				if(currentCategory != null){
					throw new ConfigurationException("Found a category nested in another category!");
				}

				String category = line.substring(0, line.length() - 1);
				config.createNewCategory(category);
				currentCategory = category;
			}

			else if(line.startsWith("}")){
				currentCategory = null;
			}
			
			else if(currentCategory != null && !line.isEmpty()){
				System.out.println(line);
				String[] args = line.split(":");
				if(args == null || args.length != 3){
					throw new ConfigurationException("Invalid attribute found!");
				}

				EnumConfigType type = EnumConfigType.valueOf(args[1].toUpperCase());
				config.add(currentCategory, args[0], type, args[2]);
			}
		}
		
		return config;
	}

	private static String readNextLine(BufferedReader reader){
		try {
			return reader.readLine();
		} catch (IOException e) {
			return null;
		}
	}
	
	private static void writeLine(BufferedWriter writer, String line){
		try{
			writer.write(line);
			writer.newLine();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
