package game.common.config;

public class ConfigEntry {

	private final String name;
	private final EnumConfigType type;
	private Object value;

	public ConfigEntry(String name, EnumConfigType type, String value){
		this.name = name;
		this.type = type;
		this.value = type.getValue(value);
	}

	public int getValueAsInt() throws ConfigurationException {
		if (type == EnumConfigType.INTEGER)
			return (Integer) value;
		else
			throw new ConfigurationException("Invalid type cast, value " + name + " is type of " + type.name() + "!");
	}
	
	public float getValueAsFloat() throws ConfigurationException { 
		if (type == EnumConfigType.FLOAT)
			return (Float) value;
		else
			throw new ConfigurationException("Invalid type cast, value " + name + " is type of " + type.name() + "!");
	}

	public boolean getValueAsBoolean() throws ConfigurationException {
		if (type == EnumConfigType.BOOLEAN)
			return (Boolean) value;
		else
			throw new ConfigurationException("Invalid type cast, value " + name + " is type of " + type.name() + "!");
	}
	
	public String getValueAsString() throws ConfigurationException {
		if (type == EnumConfigType.STRING)
			return (String) value;
		else
			throw new ConfigurationException("Invalid type cast, value " + name + " is type of " + type.name() + "!");
	}
	
	public void setValue(String value){
		this.value = type.getValue(value);
	}
	
	public String getName(){
		return name;
	}

	public Object getValue(){
		return value;
	}
	
	public String toString(){
		return name + ":" + type.name().toLowerCase() + ":" + value.toString();
	}
}
