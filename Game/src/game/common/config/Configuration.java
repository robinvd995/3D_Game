package game.common.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Configuration {

	private final String filePath;

	private HashMap<String,CategoryContainer> categoryMap = new HashMap<String,CategoryContainer>();
	
	private Configuration(String filePath){
		this.filePath = filePath;
	}

	private void createNewCategory(String category){
		categoryMap.put(category, new CategoryContainer());
	}

	private void addInteger(String cat, String name, int value){
		categoryMap.get(cat).setInteger(name, value);
	}

	private void addBoolean(String cat, String name, boolean value){
		categoryMap.get(cat).setBoolean(name, value);
	}

	private void addFloat(String cat, String name, float value){
		categoryMap.get(cat).setFloat(name, value);
	}

	private void addString(String cat, String name, String value){
		categoryMap.get(cat).setString(name, value);
	}

	public int getInteger(String cat, String name){
		return categoryMap.get(cat).getInteger(name);
	}
	
	public boolean getBoolean(String cat, String name){
		return categoryMap.get(cat).getBoolean(name);
	}
	
	public float getFloat(String cat, String name){
		return categoryMap.get(cat).getFloat(name);
	}
	
	public String getString(String cat, String name){
		return categoryMap.get(cat).getString(name);
	}
	
	public static Configuration loadConfig(String filePath){

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

				switch(args[1]){
				case "integer":
					config.addInteger(currentCategory, args[0], Integer.valueOf(args[2]));
					break;

				case "boolean":
					config.addBoolean(currentCategory, args[0], Boolean.valueOf(args[2]));
					break;

				case "float":
					config.addFloat(currentCategory, args[0], Float.valueOf(args[2]));
					break;

				case "string":
					config.addString(currentCategory, args[0], args[2]);
					break;
				}
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

	public static class ConfigurationException extends RuntimeException {

		public ConfigurationException(String s){
			super(s);
		}
	}
	
	public static class CategoryContainer {
		
		private HashMap<String,Integer> integerMap = new HashMap<String,Integer>();
		private HashMap<String,Float> floatMap = new HashMap<String,Float>();
		private HashMap<String,Boolean> boolMap = new HashMap<String,Boolean>();
		private HashMap<String,String> stringMap = new HashMap<String,String>();
		
		private CategoryContainer(){}
		
		private int getInteger(String name){
			return integerMap.get(name);
		}
		
		private float getFloat(String name){
			return floatMap.get(name);
		}
		
		private boolean getBoolean(String name){
			return boolMap.get(name);
		}
		
		private String getString(String name){
			return stringMap.get(name);
		}
		
		private void setInteger(String name, int value){
			integerMap.put(name, value);
		}
		
		private void setFloat(String name, float value){
			floatMap.put(name, value);
		}
		
		private void setBoolean(String name, boolean value){
			boolMap.put(name, value);
		}
		
		private void setString(String name, String value){
			stringMap.put(name, value);
		}
	}
}
