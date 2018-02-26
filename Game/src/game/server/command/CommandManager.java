package game.server.command;

import java.util.HashMap;

public class CommandManager {

	public static final CommandManager INSTANCE = new CommandManager();

	private volatile HashMap<String,ICommand> commandMap = new HashMap<String,ICommand>();

	private CommandManager(){
		registerCommand(new CommandChat());
	}

	public void registerCommand(ICommand command){
		String[] prefixes = command.getPrefix();
		for(String prefix : prefixes){
			commandMap.put(prefix, command);
		}
	}

	public CommandContainer getCommand(String line){
		if(line == null || line.isEmpty()){
			throw new RuntimeException("An empty command has been send, this is not supposed to happen! please tell the dev that he is an idiot!");
		}
		int index = line.indexOf(" ");
		String prefix = "";
		String sub = "";
		String[] args = new String[0];
		if(index > 0){
			prefix = line.substring(0, index).toLowerCase();
			sub = line.substring(index + 1);
			args = sub.split(" ");
		}
		else{
			prefix = line;
		}
		ICommand command = commandMap.get(prefix);
		CommandContainer container = new CommandContainer(command, args);
		if(command == null){
			container.setErrorCode(1);
			return container;
		}
		
		if(!isValidCommandArgCount(args.length, command.argumentCounts())){
			container.setErrorCode(2);
			return container;
		}
		
		return container;
	}
	
	private boolean isValidCommandArgCount(int value, int[] data){
		if(data == null || data.length == 0) return true;
		for(int i = 0; i < data.length; i++){
			if(data[i] == value) return true;
		}
		return false;
	}
}
