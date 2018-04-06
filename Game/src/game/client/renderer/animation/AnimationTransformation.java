package game.client.renderer.animation;

import caesar.util.Vector3f;
import caesar.util.interpolate.IInterpolateFunction;
import caesar.util.interpolate.InterpolateFunctions;
import caesar.util.interpolate.Interpolator;

public class AnimationTransformation {

	private final double animationTime;
	private Interpolator<Vector3f> positionInterpolator;
	private Interpolator<Vector3f> rotationInterpolator;
	private Interpolator<Vector3f> scaleInterpolator;
	
	public AnimationTransformation(double animationTime){
		this(animationTime, InterpolateFunctions.FUNCTION_LINEAR);
	}
	
	public AnimationTransformation(double animationTime, IInterpolateFunction func){
		this.animationTime = animationTime;
		this.positionInterpolator = new Interpolator<Vector3f>(animationTime, func);
		this.rotationInterpolator = new Interpolator<Vector3f>(animationTime, func);
		this.scaleInterpolator = new Interpolator<Vector3f>(animationTime, func);
	}
	
	public void addPositionFrame(Vector3f position, double time){
		positionInterpolator.addInterpolation(time, position);
	}
	
	public void addRotationFrame(Vector3f rotation, double time){
		rotationInterpolator.addInterpolation(time, rotation);
	}
	
	public void addScaleFrame(Vector3f scale, double time){
		scaleInterpolator.addInterpolation(time, scale);
	}
	
	public double getAnimationTime(){
		return animationTime;
	}
	
	public Vector3f getPosition(double time){
		return positionInterpolator.getInterpolatedValue(time);
	}
	
	public Vector3f getRotation(double time){
		return rotationInterpolator.getInterpolatedValue(time);
	}
	
	public Vector3f getScale(double time){
		return scaleInterpolator.getInterpolatedValue(time);
	}
	
	public boolean hasPositionAnimation(){
		return positionInterpolator.getSize() > 0;
	}
	
	public boolean hasRotationAnimation(){
		return rotationInterpolator.getSize() > 0;
	}
	
	public boolean hasScaleAnimation(){
		return scaleInterpolator.getSize() > 0;
	}
}
