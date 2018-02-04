package game.renderer;

import caesar.util.GlobalAxis;
import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import game.display.DisplayManager;
import game.entity.Transform;
import game.input.InputManager;
import game.input.Key;
import game.input.KeyTracker;

public class Camera {

	private Transform transform = new Transform();
	
	private float sensitivity = 0.03f;
	private float movementSpeed = 20.0f;
	
	private KeyTracker leftCtrlTracker = new KeyTracker(Key.LEFT_CTRL);
	
	public Camera(){
		transform.translate(0.0f, 0.0f, 10.0f);
	}

	public void update(double delta) {
		
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
		
		if(leftCtrlTracker.isKeyAction(1)){
			DisplayManager.INSTANCE.nextDisplaySize();
		}
	}
	
	public Matrix4f createViewMatrix(){
		return MathHelper.createViewMatrix(transform.getPosition(), transform.getOrientation());
	}
	
	private double modules(double d, double max){
		if(d > max){
			return d - max;
		}
		else if(d < 0.0f){
			return d + max;
		}
		return d;
	}
}