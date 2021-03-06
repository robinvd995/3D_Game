package game.client.renderer;

import org.lwjgl.opengl.GL11;

public class GLHelper {

	public static void enableAlphaBlending(){
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableBlending(){
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public static void clear(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
}
