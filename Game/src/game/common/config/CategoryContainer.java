package game.common.config;

import java.util.ArrayList;
import java.util.List;

public class CategoryContainer {
	
	private List<ConfigEntry> entries;
	
	protected CategoryContainer(){
		entries = new ArrayList<ConfigEntry>();
	}
	
	protected void add(String name, EnumConfigType type, String value){
		entries.add(new ConfigEntry(name, type, value));
	}
	
	public void set(String name, String value) throws ConfigurationException{
		ConfigEntry entry = getEntry(name);
		entry.setValue(value);
	}
	
	public int getInteger(String name) throws ConfigurationException{
		ConfigEntry entry = getEntry(name);
		return entry.getValueAsInt();
	}
	
	public float getFloat(String name) throws ConfigurationException{
		ConfigEntry entry = getEntry(name);
		return entry.getValueAsFloat();
	}
	
	public boolean getBoolean(String name) throws ConfigurationException{
		ConfigEntry entry = getEntry(name);
		return entry.getValueAsBoolean();
	}
	
	public String getString(String name) throws ConfigurationException{
		ConfigEntry entry = getEntry(name);
		return entry.getValueAsString();
	}
	
	private ConfigEntry getEntry(String name) throws ConfigurationException{
		for(ConfigEntry entry : entries){
			if(entry.getName().equals(name)) return entry;
		}
		throw new ConfigurationException("Could not find a value for " + name + "!");
	}
	
	protected List<ConfigEntry> getEntries(){
		return entries;
	}
}