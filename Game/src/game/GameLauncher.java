package game;

public class GameLauncher {

	public static void main(String[] args){

		Game.INSTANCE.init();

		while(!Game.INSTANCE.shouldClose()){
			Game.INSTANCE.run();
		}

		Game.INSTANCE.close();
	}
}
