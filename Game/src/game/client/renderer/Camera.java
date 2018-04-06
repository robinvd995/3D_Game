package game.client.renderer;

import caesar.util.MathHelper;
import caesar.util.Matrix4f;
import caesar.util.Vector3f;
import game.common.entity.Transform;

public class Camera {

	private Transform transform = new Transform();
	
	private static final float MAX_PITCH = (float) Math.toRadians(80.0);
	
	private float pitch = 0.0f;
	
	public Camera(){
		//transform.translate(0.0f, 0.0f, 10.0f);
	}

	/*public void update(double delta) {

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
	}*/

	public void setPosition(Vector3f position){
		transform.setPosition(position.getX(), position.getY(), position.getZ());
	}
	
	public void setCameraYaw(float yaw){
		transform.getRotation().setY(yaw);
	}
	
	public void addCameraPitch(float d){
		pitch = MathHelper.clampf(pitch + d, -MAX_PITCH, MAX_PITCH);
		transform.getRotation().setX(pitch);
	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public Matrix4f createViewMatrix(){
		return MathHelper.createViewMatrix(transform.getPosition(), transform.getOrientation());
	}
}