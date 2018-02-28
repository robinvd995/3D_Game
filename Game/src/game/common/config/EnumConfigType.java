package game.common.config;

public enum EnumConfigType {

	INTEGER,FLOAT,BOOLEAN,STRING;
	
	public Object getValue(String value) {
		switch(this){
		case INTEGER: return Integer.valueOf(value);
		case FLOAT: return Float.valueOf(value);
		case BOOLEAN: return Boolean.valueOf(value);
		default: return value;
		}
	}
}
