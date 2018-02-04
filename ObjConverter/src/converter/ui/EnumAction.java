package converter.ui;

public enum EnumAction {

	//FILE
	NEW("New", "file.new"),
	OPEN("Open", "file.open"),
	SAVE("Save", "file.save"),
	EXIT("Exit", "file.exit"),
	
	//MODIFIER
	TEXTURE("Texture", "modifier.Texture/texture"),
	
	//MODEL
	SECTIONS("Sections", "model.Sections")
	;
	
	private final String actionName;
	private final String actionCommand;
	
	private EnumAction(String actionName, String actionCommand){
		this.actionName = actionName;
		this.actionCommand = actionCommand;
	}
	
	public String getActionCommand(){
		return actionCommand;
	}
	
	public String getActionName(){
		return actionName;
	}
	
	public static EnumAction getActionFromCommand(String command){
		for(EnumAction action : values()){
			if(command.equals(action.getActionCommand())){
				return action;
			}
		}
		return null;
	}
}
