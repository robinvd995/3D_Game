package game.server.command;

import game.server.io.Logger;

public class CommandContainer {

	private final ICommand command;
	private final String[] args;
	
	private int errorCode;
	
	public CommandContainer(ICommand command, String[] args){
		this.command = command;
		this.args = args;
	}
	
	public void execute(){
		command.execute(args);
	}
	
	protected void setErrorCode(int error){
		this.errorCode = error;
	}
	
	public boolean isValid(){
		return errorCode == 0;
	}

	public void logError() {
		Logger.logError(getErrorMessage());
	}
	
	private String getErrorMessage(){
		switch(errorCode){
		default: return "An unexpected error has occured!";
		case 1: return "Unknown command!";
		case 2: return "Invalid amount of arguments!";
		}
	}
}
