/*package game.scene;

import java.util.HashMap;

public class SceneManager {

	private final HashMap<String,Class<? extends IScene>> sceneMap;
	
	private String sceneToLoad = "";
	
	public SceneManager(){
		sceneMap = new HashMap<String,Class<? extends IScene>>();
	}
	
	public void init(){
		loadScene("main_menu", SceneMenu.class);
		loadScene("game", SceneGame.class);
	}
	
	private void loadScene(String id, Class<? extends IScene> clzz){
		sceneMap.put(id, clzz);
	}
	
	public IScene createSceneToLoad(){
		Class<? extends IScene> clzz = sceneMap.get(sceneToLoad);
		try {
			IScene scene = clzz.newInstance();
			sceneToLoad = "";
			return scene;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean hasSceneToLoad(){
		return !sceneToLoad.isEmpty();
	}
	
	public void loadScene(String scene){
		sceneToLoad = scene;
	}
}
*/