package game.server.command;

import game.server.io.Logger;

public class CommandChat implements ICommand{

	@Override
	public String[] getPrefix() {
		return new String[]{"chat", "c"};
	}

	@Override
	public void execute(String[] args) {
		String message = "";
		for(String s : args){
			message = message + s + " ";
		}
		Logger.logInfo(message);
	}

	@Override
	public int[] argumentCounts() {
		return null;
	}

}
