package converter.model.modifier;

import java.util.HashMap;

public class ModelModifier implements IModelModifier{

	private final HashMap<String,ModifierAttribute> attribMap = new HashMap<String,ModifierAttribute>();
	
	public final String name;
	public final String id;
	
	public ModelModifier(String name, String id){
		this.name = name;
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public HashMap<String, ModifierAttribute> getAttributeMap() {
		return attribMap;
	}

}
