package game.client.renderer;

import caesar.util.GlobalAxis;
import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import game.client.Game;
import game.client.display.DisplayManager;
import game.client.input.InputManager;
import game.client.input.Key;
import game.client.input.KeyTracker;
import game.client.renderer.gui.GuiInventory;
import game.client.renderer.gui.GuiMenu;
import game.client.renderer.gui.event.GuiEvent.GuiOpenEvent;
import game.client.renderer.texture.TextureLoader;
import game.common.entity.Transform;
import game.common.event.EventManager;

public class Camera {

	private Transform transform = new Transform();

	private float sensitivity = 0.03f;
	private float movementSpeed = 20.0f;

	private KeyTracker leftCtrlTracker = new KeyTracker(Key.LEFT_CTRL);
	private KeyTracker screenShotTracker = new KeyTracker(Key.F12);
	private KeyTracker menuKeyTracker = new KeyTracker(Key.ESC);
	private KeyTracker inventoryKeyTracker = new KeyTracker(Key.E);
	
	public Camera(){
		transform.translate(0.0f, 0.0f, 10.0f);
	}

	public void update(double delta) {

		if(!Game.INSTANCE.isMouseDisabled()){
			double rotX = Math.toRadians(InputManager.getMouseDeltaY() * sensitivity);
			double rotY = Math.toRadians(InputManager.getMouseDeltaX() * sensitivity);
			transform.rotate((float)rotX, (float)rotY, 0.0f);
		}
		
		if(!Game.INSTANCE.isPlayerMovementDisabled()){

			if(InputManager.isKeyDown(Key.W)){
				transform.translateInversed(GlobalAxis.Z.toVector(), (float) (-movementSpeed * delta));
			}
			if(InputManager.isKeyDown(Key.S)){
				transform.translateInversed(GlobalAxis.Z.toVector(), (float) (movementSpeed * delta));
			}
			if(InputManager.isKeyDown(Key.A)){
				transform.translateInversed(GlobalAxis.X.toVector(), (float) (-movementSpeed * delta));
			}
			if(InputManager.isKeyDown(Key.D)){
				transform.translateInversed(GlobalAxis.X.toVector(), (float) (movementSpeed * delta));
			}

		}

		if(leftCtrlTracker.isKeyAction(1)){
			DisplayManager.INSTANCE.nextDisplaySize();
		}

		if(screenShotTracker.isKeyAction(1)){
			TextureLoader.takeScreenshot();
		}
		
		if(menuKeyTracker.isKeyAction(1)){
			EventManager.postPreUpdateEvent(new GuiOpenEvent(new GuiMenu()));
		}
		
		if(inventoryKeyTracker.isKeyAction(1)){
			EventManager.postPreUpdateEvent(new GuiOpenEvent(new GuiInventory()));
		}
	}

	public Matrix4f createViewMatrix(){
		return MathHelper.createViewMatrix(transform.getPosition(), transform.getOrientation());
	}
}