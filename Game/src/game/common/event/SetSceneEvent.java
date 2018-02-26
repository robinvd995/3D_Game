package game.common.event;

import game.client.scene.IScene;

public class SetSceneEvent {

	private IScene scene;
	
	public SetSceneEvent(IScene scene){
		this.scene = scene;
	}
	
	public IScene getScene(){
		return scene;
	}
}
