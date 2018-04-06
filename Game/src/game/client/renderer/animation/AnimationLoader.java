package game.client.renderer.animation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import caesar.util.Vector3f;
import caesar.util.interpolate.InterpolateFunctions;
import game.common.json.JsonLoader;

public class AnimationLoader {

	public static AnimationTransformation loadTransformationAnimation(String file){
		TransformationAnimationContainer tac = null;
		try {
			tac = JsonLoader.loadClassFromJson(file, TransformationAnimationContainer.class);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(tac != null){
			AnimationTransformation anim = new AnimationTransformation(tac.animationTime, InterpolateFunctions.FUNCTION_SMOOTHSTEP);
			for(TransformationFrame posFrame : tac.position){
				Vector3f pos = new Vector3f(posFrame.x, posFrame.y, posFrame.z);
				anim.addPositionFrame(pos, posFrame.time);
			}
			for(TransformationFrame rotFrame : tac.rotation){
				Vector3f rot = new Vector3f(rotFrame.x, rotFrame.y, rotFrame.z);
				anim.addRotationFrame(rot, rotFrame.time);
			}
			for(TransformationFrame sclFrame : tac.scale){
				Vector3f rot = new Vector3f(sclFrame.x, sclFrame.y, sclFrame.z);
				anim.addScaleFrame(rot, sclFrame.time);
			}
			return anim;
		}
		else{
			return new AnimationTransformation(1.0D);
		}
	}
	
	private static class TransformationAnimationContainer{
		
		private double animationTime;
		private List<TransformationFrame> position;
		private List<TransformationFrame> rotation;
		private List<TransformationFrame> scale;
		
	}
	
	private static class TransformationFrame {
		
		private float x;
		private float y;
		private float z;
		private double time;
		
		private TransformationFrame(float x, float y, float z, double time){
			this.x = x;
			this.y = y;
			this.z = z;
			this.time = time;
		}
	}
}
