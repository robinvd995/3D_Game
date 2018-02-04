package game.entity;

import caesar.util.GlobalAxis;
import game.input.InputManager;
import game.input.Key;
import game.input.KeyTracker;
import game.world.World;

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
