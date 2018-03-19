package game.server;

import game.common.CommonHandler;
import game.common.EnumSide;
import game.server.gui.ServerGui;
import game.server.io.Logger;

public class ServerLauncher {

	public static void main(String[] args){
		
		CommonHandler.init(EnumSide.SERVER);
		
		Logger.initOutputStreams("SERVER");
		
		ServerGui.initGui();
		while(!ServerGui.isInitialzied());
		
		Server.INSTANCE.init();

		Server.INSTANCE.start();
		while(Server.INSTANCE.isRunning()){
			Server.INSTANCE.run();
		}
		Server.INSTANCE.stop();
	}
}
