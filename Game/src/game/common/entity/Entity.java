package game.common.entity;

import java.util.LinkedList;
import java.util.List;

import caesar.util.Vector3f;
import game.client.renderer.DebugRenderer;
import game.client.renderer.debug.DebugAxisAlignedBB;
import game.client.renderer.debug.DebugRay;
import game.common.physics.AxisAlignedBB;
import game.common.physics.Ray;
import game.common.util.BlockPos;
import game.common.util.EnumDirection;
import game.common.world.World;
import game.common.world.cluster.Cluster;
import game.common.world.cluster.ClusterPosition;

public class Entity {

	protected Transform transform = new Transform();

	private Vector3f lastPosition;

	private boolean isGravityEnabled = true;
	private boolean isGrounded = false;

	private AxisAlignedBB aabb;

	private World world;

	private float velocityY;
	
	public Entity(){
		aabb = new AxisAlignedBB(-0.5f, 0.0f, -0.5f, 0.5f, 2.0f, 0.5f);
	}

	public void init(){
		lastPosition = transform.getPosition().copy();
	}

	public void update(double delta){
		lastPosition = transform.getPosition().copy();
	}

	public void lateUpdate(double delta){

		if(isGravityEnabled() && !isGrounded){
			velocityY += -world.getGravity() * delta;
		}
		
		transform.translate(0.0f, velocityY, 0.0f);
		
		/*Ray ray = new Ray(transform.getPosition(), transform.getOrientation(), 10.0f);
		DebugRenderer.INSTANCE.addObjectToRender(new DebugRay(ray));*/

		int minX = (int) Math.floor(transform.getPosition().getX() + aabb.getMinX());
		int maxX = (int) Math.ceil(transform.getPosition().getX() + aabb.getMaxX());
		int minY = (int) Math.floor(transform.getPosition().getY() + aabb.getMinY());
		int maxY = (int) Math.ceil(transform.getPosition().getY() + aabb.getMaxY());
		int minZ = (int) Math.floor(transform.getPosition().getZ() + aabb.getMinZ());
		int maxZ = (int) Math.ceil(transform.getPosition().getZ() + aabb.getMaxZ());

		for(int i = minX; i < maxX; i++){
			for(int j = minY; j < maxY; j++){
				for(int k = minZ; k < maxZ; k++){
					BlockPos pos = new BlockPos(i, j, k);
					AxisAlignedBB otherAABB = world.getBlock(pos).getBounds(world, pos);
					if(otherAABB == null)
						continue;

					DebugRenderer.INSTANCE.addObjectToRender(new DebugAxisAlignedBB(otherAABB, pos.toVector()));

					Vector3f myPos = transform.getPosition();
					Vector3f otherPos = pos.toVector();

					if(aabb.intersect(otherAABB, myPos, otherPos)){

						List<EnumDirection> possibleCollisionFaces = new LinkedList<EnumDirection>();

						float myMinX = lastPosition.getX() + aabb.getMinX();
						float myMinZ = lastPosition.getZ() + aabb.getMinZ();
						float myMaxX = lastPosition.getX() + aabb.getMaxX();
						float myMaxZ = lastPosition.getZ() + aabb.getMaxZ();

						float myMinY = lastPosition.getY() + aabb.getMinY();
						float myMaxY = lastPosition.getY() + aabb.getMaxY();

						float otherMinX = otherPos.getX() + otherAABB.getMinX();
						float otherMinZ = otherPos.getZ() + otherAABB.getMinZ();
						float otherMaxX = otherPos.getX() + otherAABB.getMaxX();
						float otherMaxZ = otherPos.getZ() + otherAABB.getMaxZ();

						float otherMinY = otherPos.getY() + otherAABB.getMinY();
						float otherMaxY = otherPos.getY() + otherAABB.getMaxY();

						boolean isLeft = myMaxX > otherMinX;
						boolean isRight = myMinX < otherMaxX;
						boolean isBack = myMaxZ > otherMinZ;
						boolean isFront = myMinZ < otherMaxZ;

						boolean isUp = myMinY < otherMaxY;
						boolean isDown = myMaxY > otherMinY;

						if(isLeft && isBack && isFront && isUp && isDown && !isRight){
							possibleCollisionFaces.add(EnumDirection.LEFT);
						}
						if(isRight && isBack && isFront && isUp && isDown && !isLeft){
							possibleCollisionFaces.add(EnumDirection.RIGHT);
						}
						if(isBack && isLeft && isRight && isUp && isDown && !isFront){
							possibleCollisionFaces.add(EnumDirection.BACK);
						}
						if(isFront && isLeft && isRight && isUp && isDown && !isBack){
							possibleCollisionFaces.add(EnumDirection.FRONT);
						}
						if(isUp && isLeft && isRight && isFront && isBack && !isDown){
							possibleCollisionFaces.add(EnumDirection.UP);
						}
						if(isDown && isLeft && isRight && isFront && isBack && !isUp){
							possibleCollisionFaces.add(EnumDirection.DOWN);
						}

						float newPosX = 0;
						float newPosZ = 0;

						for(EnumDirection dir : possibleCollisionFaces){

							switch(dir){
							default: break;
							case LEFT:
								//if(myMinX >= otherMaxX){
									newPosX = otherPos.getX() + otherAABB.getMaxX() - aabb.getMinX();
									transform.getPosition().setX(newPosX);
								//}
								break;
							case RIGHT:
								//if(true){
									newPosX = otherPos.getX() + otherAABB.getMinX() - aabb.getMaxX();
									transform.getPosition().setX(newPosX);
								//}
								break;
							case FRONT:
								//if(true){
									newPosZ = otherPos.getZ() + otherAABB.getMinZ() - aabb.getMaxZ();
									transform.getPosition().setZ(newPosZ);
								//}
								break;
							case BACK:
								//if(myMinZ >= otherMaxZ){
									newPosZ = otherPos.getZ() + otherAABB.getMaxZ() - aabb.getMinZ();
									transform.getPosition().setZ(newPosZ);
								//}
								break;
							case UP:
								transform.getPosition().setY(lastPosition.getY());
								break;
							case DOWN:
								transform.getPosition().setY(lastPosition.getY());
								isGrounded = true;
								velocityY = 0;
								break;
							}
						}

						/*float relX = lastPosition.getX() - pos.getX();
						float relZ = lastPosition.getZ() - pos.getZ();

						EnumDirection leftRightSide = relX < 0 ? EnumDirection.LEFT : EnumDirection.RIGHT;
						EnumDirection frontBackSide = relZ < 0 ? EnumDirection.BACK : EnumDirection.FRONT;

						float correctedX = 0.0f;
						float correctedZ = 0.0f;

						if(leftRightSide == EnumDirection.LEFT){
							correctedX = otherPos.getX() + otherAABB.getMinX() - aabb.getMaxX();
							System.out.println("left");
						}
						else{
							correctedX = otherPos.getX() + otherAABB.getMaxX() - aabb.getMinX();
							System.out.println("right");
						}

						if(frontBackSide == EnumDirection.BACK){

						}
						else{

						}*/

						//transform.getPosition().set(lastPosition.getX(), lastPosition.getY(), lastPosition.getZ());
					}
				}
			}
		}

		/*for(int x = 0; x < world.getMaxX(); x++){
			for(int y = 0; y < world.getMaxY(); y++){
				for(int z = 0; z < world.getMaxZ(); z++){
					BlockPos pos = new BlockPos(x, y, z);
					AxisAlignedBB otherAABB = world.getBlock(pos).getBoundingBox();
					if(otherAABB == null)
						continue;

					RayAABBResult result = ray.intersectRayWithAxisAlignedBB(otherAABB, pos.toVector());
					if(result.intersects()){
						DebugRenderer.INSTANCE.addObjectToRender(new DebugPosition(result.getPoint()));
					}
				}
			}
		}*/

		//Collision
		/*int minX = (int) Math.floor(transform.getPosition().getX() + aabb.getMinX());
		int maxX = (int) Math.ceil(transform.getPosition().getX() + aabb.getMaxX());
		int minY = (int) Math.floor(transform.getPosition().getY() + aabb.getMinY());
		int maxY = (int) Math.ceil(transform.getPosition().getY() + aabb.getMaxY());
		int minZ = (int) Math.floor(transform.getPosition().getZ() + aabb.getMinZ());
		int maxZ = (int) Math.ceil(transform.getPosition().getZ() + aabb.getMaxZ());

		boolean collidedWithYMin = false;

		Ray collisionRay = new Ray(transform.getPosition(), transform.getOrientation(), 10.0f);

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

						RayAABBResult result = collisionRay.intersectRayWithAxisAlignedBB(otherAABB, otherPos);
						if(result.intersects()){
							Vector3f intersection = result.getPoint();
							EnumDirection side = result.getCollisionSide();
							float newPosX = intersection.getX() + (float)side.getOffsetX() * 0.51f;
							float newPosY = intersection.getY() + (float)side.getOffsetY() * 0.51f;
							float newPosZ = intersection.getZ() + (float)side.getOffsetZ() * 0.51f;
							transform.getPosition().set(newPosX, newPosY, newPosZ);
						}
						/*float newPosX = 0.0f;
						float newPosY = 0.0f;
						float newPosZ = 0.0f;

						float myMinX = aabb.getMinX() + myPos.getX();
						float myMinZ = aabb.getMinZ() + myPos.getZ();
						float myMaxX = aabb.getMaxX() + myPos.getX();
						float myMaxZ = aabb.getMaxZ() + myPos.getZ();

						float otherMinX = otherAABB.getMinX() + otherPos.getX();
						float otherMinZ = otherAABB.getMinZ() + otherPos.getZ();
						float otherMaxX = otherAABB.getMaxX() + otherPos.getX();
						float otherMaxZ = otherAABB.getMaxZ() + otherPos.getZ();*/



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
		/*}
				}
			}

			if(!collidedWithYMin){
				isGrounded = false;
			}
		}

		velocity = transform.getPosition().copy().min(lastPosition);
		DebugRenderer.INSTANCE.addObjectToRender(new DebugAxisAlignedBB(aabb, transform.getPosition()));
		DebugRenderer.INSTANCE.addObjectToRender(new DebugPosition(transform.getPosition()));*/
	}

	public void onEntitySpawned(World world){
		this.world = world;
	}
	
	public void jump(float jumpStrength){
		if(isGrounded){
			velocityY = jumpStrength;
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

	public ClusterPosition getClusterCoords(){
		int x = ((int)transform.getPosition().getX()) / Cluster.CLUSTER_SIZE;
		int y = ((int)transform.getPosition().getY()) / Cluster.CLUSTER_SIZE;
		int z = ((int)transform.getPosition().getZ()) / Cluster.CLUSTER_SIZE;
		return new ClusterPosition(x, y, z);
	}
}
