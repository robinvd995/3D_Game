package converter.model.modifier;

public class ModifierAttribute {

	private static final int TYPE_INT = 0;
	private static final int TYPE_FLOAT = 1;
	private static final int TYPE_DOUBLE = 2;
	private static final int TYPE_STRING = 3;

	private final int type;
	private final Object attribute;

	public ModifierAttribute(int attribute){
		this.attribute = Integer.valueOf(attribute);
		this.type = TYPE_INT;
	}

	public ModifierAttribute(float attribute){
		this.attribute = Float.valueOf(attribute);
		this.type = TYPE_FLOAT;
	}

	public ModifierAttribute(double attribute){
		this.attribute = Double.valueOf(attribute);
		this.type = TYPE_DOUBLE;
	}

	public ModifierAttribute(String attribute){
		this.attribute = attribute;
		this.type = TYPE_STRING;
	}

	public int getInteger(){
		if(type == TYPE_INT){
			return (int) attribute;
		}
		else{
			throw new IllegalArgumentException("Attribute is not a type of Integer!");
		}
	}
	
	public float getFloat(){
		if(type == TYPE_FLOAT){
			return (float) attribute;
		}
		else{
			throw new IllegalArgumentException("Attribute is not a type of Float!");
		}
	}
	
	public double getDouble(){
		if(type == TYPE_DOUBLE){
			return (double) attribute;
		}
		else{
			throw new IllegalArgumentException("Attribute is not a type of Double!");
		}
	}
	
	public String getString(){
		if(type == TYPE_STRING){
			return (String) attribute;
		}
		else{
			throw new IllegalArgumentException("Attribute is not a type of String!");
		}
	}
}
