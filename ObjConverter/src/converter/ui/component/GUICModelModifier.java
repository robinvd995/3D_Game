package converter.ui.component;

import java.util.HashMap;

import javax.swing.JPanel;

import converter.model.modifier.IModelModifier;
import converter.model.modifier.ModifierAttribute;
import converter.ui.ModelScene;

public abstract class GUICModelModifier {

	private final ModelScene theScene;
	private final IModelModifier theModifier;

	public GUICModelModifier(ModelScene scene, IModelModifier modifier){
		this.theScene = scene;
		this.theModifier = modifier;
	}

	public abstract void initModifierGui(JPanel panel);

	public abstract int getPanelHeight();

	public void apply(){
		//TODO
	}
	
	public void remove(){
		//TODO
	}

	public void addValue(String key, int value){
		HashMap<String,ModifierAttribute> attribMap = theModifier.getAttributeMap();
		attribMap.put(key, new ModifierAttribute(value));
	}

	public void addValue(String key, float value){
		HashMap<String,ModifierAttribute> attribMap = theModifier.getAttributeMap();
		attribMap.put(key, new ModifierAttribute(value));
	}

	public void addValue(String key, double value){
		HashMap<String,ModifierAttribute> attribMap = theModifier.getAttributeMap();
		attribMap.put(key, new ModifierAttribute(value));
	}

	public void addValue(String key, String value){
		HashMap<String,ModifierAttribute> attribMap = theModifier.getAttributeMap();
		attribMap.put(key, new ModifierAttribute(value));
	}
}
