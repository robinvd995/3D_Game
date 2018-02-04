package converter.model.modifier;

import java.util.HashMap;

public interface IModelModifier {

	String getName();
	String getId();
	HashMap<String,ModifierAttribute> getAttributeMap(); 
}
