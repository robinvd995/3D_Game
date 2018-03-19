package game.client;

import game.common.CommonHandler;
import game.common.EnumSide;

public class GameLauncher {

	public static void main(String[] args){
		
		CommonHandler.init(EnumSide.CLIENT);
		
		Game.INSTANCE.init();
		
		while(!Game.INSTANCE.shouldClose()){
			Game.INSTANCE.run();
		}

		Game.INSTANCE.close();
	}
}
