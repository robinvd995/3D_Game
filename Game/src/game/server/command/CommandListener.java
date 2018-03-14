package game.server.command;

import java.io.IOException;
import java.util.Scanner;

import game.server.Server;
import game.server.io.Logger;

public class CommandListener extends Thread{

	private static CommandListener listener;

	private volatile boolean isRunning = false;
	private volatile Scanner scanner;

	private CommandListener(){}

	private void init(){
		scanner = new Scanner(System.in);
		setName("Command-Listener");
		isRunning = true;
	}

	@Override
	public void run(){
		while(isRunning){
			try {
				if(hasNextLine()){
					String line = scanner.nextLine();
					Logger.logInfo(line);
					Server.INSTANCE.addCommand(line);
				}
			} catch (IOException e) {}
		}

		scanner.close();
	}

	private boolean hasNextLine() throws IOException{
		while(System.in.available() == 0){
			try{
				sleep(50);
			} catch(InterruptedException e){
				return false;
			}
		}
		return scanner.hasNextLine();
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

	public static void stopCommandListener(){
		if(listener != null){
			listener.isRunning = false;
			listener.interrupt();
			try {
				listener.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
