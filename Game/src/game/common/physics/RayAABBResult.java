package game.common.physics;

import caesar.util.Vector3f;
import game.common.util.EnumDirection;

public class RayAABBResult extends RayResult{

	private final EnumDirection collisionSide;
	
	public RayAABBResult(boolean intersects, Vector3f point, EnumDirection side) {
		super(intersects, point);
		this.collisionSide = side;
	}

	public EnumDirection getCollisionSide(){
		return collisionSide;
	}
}
