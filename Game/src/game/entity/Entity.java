package game.entity;

import java.util.ArrayList;
import java.util.List;

import caesar.util.Vector3f;
import game.physics.AxisAlignedBB;
import game.renderer.DebugRenderer;
import game.renderer.debug.DebugAxisAlignedBB;
import game.renderer.debug.DebugPosition;
import game.util.BlockPos;
import game.util.EnumDirection;
import game.world.World;

public class Entity {

	protected Transform transform = new Transform();

	private Vector3f lastPosition;

	private boolean isGravityEnabled = false;
	private boolean isGrounded = false;

	private float gravity = 0.5f;

	private Vector3f velocity = new Vector3f();

	private AxisAlignedBB aabb;

	private World world;

	public Entity(World world){
		this.world = world;
		aabb = new AxisAlignedBB(-0.5f, 0.0f, -0.5f, 0.5f, 2.0f, 0.5f);
	}

	public void init(){
		lastPosition = transform.getPosition().copy();
	}

	public void update(double delta){
		lastPosition = transform.getPosition().copy();
		if(isGravityEnabled() && !isGrounded){
			transform.translate(0.0f, velocity.getY() - (float)(gravity * delta), 0.0f);
		}
	}

	public void lateUpdate(double delta){

		//Collision
		int minX = (int) Math.floor(transform.getPosition().getX() + aabb.getMinX());
		int maxX = (int) Math.ceil(transform.getPosition().getX() + aabb.getMaxX());
		int minY = (int) Math.floor(transform.getPosition().getY() + aabb.getMinY());
		int maxY = (int) Math.ceil(transform.getPosition().getY() + aabb.getMaxY());
		int minZ = (int) Math.floor(transform.getPosition().getZ() + aabb.getMinZ());
		int maxZ = (int) Math.ceil(transform.getPosition().getZ() + aabb.getMaxZ());

		boolean collidedWithYMin = false;

		for(int i = minX; i < maxX; i++){
			for(int j = minY; j < maxY; j++){
				for(int k = minZ; k < maxZ; k++){
					BlockPos pos = new BlockPos(i, j, k);
					AxisAlignedBB otherAABB = world.getBlock(pos).getBoundingBox();
					if(otherAABB == null)
						continue;

					DebugRenderer.INSTANCE.addObjectToRender(new DebugAxisAlignedBB(otherAABB, pos.toVector()));

					Vector3f myPos = transform.getPosition();
					Vector3f otherPos = pos.toVector();
					if(aabb.intersect(otherAABB, myPos, otherPos)){
						
						float newPosX = 0.0f;
						float newPosY = 0.0f;
						float newPosZ = 0.0f;
						
						float myMinX = aabb.getMinX() + myPos.getX();
						float myMinZ = aabb.getMinZ() + myPos.getZ();
						float myMaxX = aabb.getMaxX() + myPos.getX();
						float myMaxZ = aabb.getMaxZ() + myPos.getZ();
						
						float otherMinX = otherAABB.getMinX() + otherPos.getX();
						float otherMinZ = otherAABB.getMinZ() + otherPos.getZ();
						float otherMaxX = otherAABB.getMaxX() + otherPos.getX();
						float otherMaxZ = otherAABB.getMaxZ() + otherPos.getZ();
						
						
						
						/*for(EnumDirection horDir : EnumDirection.getHorizontalDirections()){
							boolean collided = false;
							
							
							switch(horDir){
							case LEFT:
								if(aabb.getMinX() + myPos.getX() <= otherAABB.getMaxX() + otherPos.getX()){
									newPosX = otherPos.getX() + otherAABB.getMaxX() - aabb.getMinX();
									velocity.setX(0.0f);
									collided = true;
									transform.getPosition().setX(newPosX);
									System.out.println(horDir.name());
								}
								break;
							case RIGHT:
								if(aabb.getMaxX() + myPos.getX() >= otherAABB.getMinX() + otherPos.getX()){
									newPosX = otherPos.getX() + otherAABB.getMinX() - aabb.getMaxX();
									velocity.setX(0.0f);
									collided = true;
									transform.getPosition().setX(newPosX);
									System.out.println(horDir.name());
								}
								break;
							case FRONT:
								if(aabb.getMinZ() + myPos.getZ() <= otherAABB.getMaxZ() + otherPos.getZ()){
									newPosZ = otherPos.getZ() + otherAABB.getMaxZ() - aabb.getMinZ();
									velocity.setZ(0.0f);
									collided = true;
									transform.getPosition().setZ(newPosZ);
									System.out.println(horDir.name());
								}
								break;
							case BACK:
								if(aabb.getMaxZ() + myPos.getZ() >= otherAABB.getMinZ() + otherPos.getZ()){
									newPosZ = otherPos.getZ() + otherAABB.getMinZ() - aabb.getMaxZ();
									velocity.setZ(0.0f);
									collided = true;
									transform.getPosition().setZ(newPosX);
									System.out.println(horDir.name());
								}
								break;
							default: break;
							}
							
							if(collided){
								break;
							}
						}*/
						/*if(aabb.getMinY() + myPos.getY() <= otherAABB.getMaxY() + otherPos.getY()){
							collidedWithYMin = true;
							isGrounded = true;
							velocity.setY(0.0f);
							transform.getPosition().setY(otherAABB.getMaxY() + otherPos.getY()
							);
						}*/
					}
				}
			}

			if(!collidedWithYMin){
				isGrounded = false;
			}
		}
		
		velocity = transform.getPosition().copy().min(lastPosition);
		DebugRenderer.INSTANCE.addObjectToRender(new DebugAxisAlignedBB(aabb, transform.getPosition()));
		DebugRenderer.INSTANCE.addObjectToRender(new DebugPosition(transform.getPosition()));
	}
	
	private List<EnumDirection> getValidHorizontalCollisionSides(Vector3f myPos, Vector3f otherPos){
		List<EnumDirection> list = new ArrayList<EnumDirection>();
		
		/*if(myPos.getX() < otherPos.getX()){
			list.add(EnumDirection.RIGHT);
		}
		if(myPos.getX() > otherPos.getX()){
			list.add(EnumDirection.LEFT);
		}*/
		if(myPos.getZ() < otherPos.getZ()){
			list.add(EnumDirection.FRONT);
		}
		if(myPos.getZ() > otherPos.getZ()){
			list.add(EnumDirection.BACK);
		}
		
		// get vector3f from angle of the entity to the aabb center
		// -45 - 45 is top, 45 - 135 is right enz.
		// May not work when the aabb is a rectangle.
		// maybe getting the closest relative may work,
		// int closest = 9999999;
		// for directions
		//		distance = direction.length player dist
		//		distance < closest
		//		closest = distance
		
		
		return list;
	}
	
	public void jump(float jumpStrength){
		if(isGrounded){
			transform.getPosition().add(0.0f, jumpStrength, 0.0f);
			isGrounded = false;
		}
	}

	public void setGravityEnabled(boolean enabled){
		this.isGravityEnabled = enabled;
	}

	public boolean isGravityEnabled(){
		return isGravityEnabled;
	}

	public Transform getTransform(){
		return transform;
	}

	public AxisAlignedBB getBoundingBox(){
		return aabb;
	}
}
