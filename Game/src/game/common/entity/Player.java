package game.common.entity;

import caesar.util.GlobalAxis;
import game.client.input.InputManager;
import game.client.input.Key;
import game.client.input.KeyTracker;
import game.common.world.World;

public class Player extends Entity{

	private final float movementSpeed = 10.0f;
	private final float rotationSpeed = 1.0f;
	private final float jumpStrength = 1.0f;
	
	private final KeyTracker jumpTracker = new KeyTracker(Key.SPACE);
	
	public Player(World world){
		super(world);
	}

	@Override
	public void update(double delta){
		
		super.update(delta);
		
		if(InputManager.isKeyDown(Key.UP)){
			transform.translate(GlobalAxis.Z.toVector(), (float) (movementSpeed * delta));
		}

		if(InputManager.isKeyDown(Key.DOWN)){
			transform.translate(GlobalAxis.Z.toVector(), (float) (-movementSpeed * delta));
		}
		
		if(InputManager.isKeyDown(Key.MOVE_UP)){
			System.out.println("up");
			transform.translate(GlobalAxis.Y.toVector(), (float) (movementSpeed * delta));
		}
		
		if(InputManager.isKeyDown(Key.MOVE_DOWN)){
			System.out.println("down");
			transform.translate(GlobalAxis.Y.toVector(), (float) (-movementSpeed * delta));
		}

		if(InputManager.isKeyDown(Key.LEFT)){
			transform.rotate(0.0f, (float) (rotationSpeed * delta), 0.0f);
		}

		if(InputManager.isKeyDown(Key.RIGHT)){
			transform.rotate(0.0f, (float) (-rotationSpeed * delta), 0.0f);
		}
		
		if(jumpTracker.isKeyAction(1)){
			this.jump(jumpStrength);
		}
	}
}
