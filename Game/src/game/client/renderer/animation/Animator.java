package game.client.renderer.animation;

import caesar.util.GlobalAxis;
import caesar.util.Matrix4f;
import caesar.util.Vector3f;

public class Animator {

	private double animationTime;
	private AnimationTransformation animation;

	private boolean isLooping = false;
	private boolean isPlaying = false;

	public Animator() {}

	public void animate(double delta, Matrix4f matrix){
		if(isPlaying && animation != null){
			if(isLooping){
				animationTime = (animationTime + delta) % animation.getAnimationTime();
			}
			else{
				animationTime += delta;
				if(animationTime >= animation.getAnimationTime()){
					animationTime = 0;
					isPlaying = false;
				}
			}

			if(animation.hasPositionAnimation()){
				Vector3f position = animation.getPosition(animationTime);
				Matrix4f.translate(position, matrix, matrix);
			}
			if(animation.hasRotationAnimation()){
				Vector3f rotation = animation.getRotation(animationTime);
				Matrix4f.rotate(rotation.getX(), GlobalAxis.X.toVector(), matrix, matrix);
				Matrix4f.rotate(rotation.getY(), GlobalAxis.Y.toVector(), matrix, matrix);
				Matrix4f.rotate(rotation.getZ(), GlobalAxis.Z.toVector(), matrix, matrix);
			}
			if(animation.hasScaleAnimation()){
				Vector3f scale = animation.getScale(animationTime);
				Matrix4f.scale(scale, matrix, matrix);
			}
		}
	}

	public void setAnimation(AnimationTransformation animation){
		this.isPlaying = true;
		this.animation = animation;
	}
	
	public boolean isPlaying(){
		return isPlaying;
	}

	public void play(){
		this.isPlaying = true;
		System.out.println(isPlaying);
	}

	public void stop(){
		this.isPlaying = false;
	}

	public void setLooping(boolean looping){
		this.isLooping = looping;
	}
}
