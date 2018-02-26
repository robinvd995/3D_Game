package game.server.command;

import java.util.Scanner;

import game.server.Server;
import game.server.io.Logger;

public class CommandListener extends Thread{

	private static CommandListener listener;
	
	private Scanner scanner;
	
	private CommandListener(){}
	
	private void init(){
		scanner = new Scanner(System.in);
	}
	
	@Override
	public void run(){
		while(Server.INSTANCE.isRunning()){
			while(scanner.hasNext()){
				String line = scanner.nextLine();
				Logger.logInfo(line);
				Server.INSTANCE.addCommand(line);
			}
		}
	}
	
	public static void createCommandListener(){
		if(listener == null){
			listener = new CommandListener();
			listener.init();
			listener.start();
		}
		else{
			throw new RuntimeException("Already instantiated a command listener!");
		}
	}
}
