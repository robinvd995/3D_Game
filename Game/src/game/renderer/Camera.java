package game.renderer;

import caesar.util.GlobalAxis;
import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import game.display.DisplayManager;
import game.entity.Transform;
import game.input.InputManager;
import game.input.Key;
import game.input.KeyTracker;
import game.renderer.texture.TextureLoader;

public class Camera {

	private Transform transform = new Transform();

	private float sensitivity = 0.03f;
	private float movementSpeed = 20.0f;

	private KeyTracker leftCtrlTracker = new KeyTracker(Key.LEFT_CTRL);
	private KeyTracker screenShotTracker = new KeyTracker(Key.F12);
	private KeyTracker menuKeyTracker = new KeyTracker(Key.ESC);

	private boolean active;

	public Camera(){
		transform.translate(0.0f, 0.0f, 10.0f);
		active = DisplayManager.INSTANCE.isCursorsDisabled();
	}

	public void update(double delta) {

		if(active){

			double rotX = Math.toRadians(InputManager.getMouseDeltaY() * sensitivity);
			double rotY = Math.toRadians(InputManager.getMouseDeltaX() * sensitivity);

			transform.rotate((float)rotX, (float)rotY, 0.0f);

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
			DisplayManager.INSTANCE.toggleCursor();
			active = DisplayManager.INSTANCE.isCursorsDisabled();
		}
	}

	public Matrix4f createViewMatrix(){
		return MathHelper.createViewMatrix(transform.getPosition(), transform.getOrientation());
	}
}