package game.test;

import org.junit.Test;

import game.client.renderer.animation.AnimationLoader;
import game.client.renderer.animation.AnimationTransformation;

public class AnimationTest {

	@Test
	public void testAnimationJsonDeserialization(){
		/*TransformationAnimationContainer tac = new TransformationAnimationContainer();
		tac.animationTime = 5.0d;
		tac.position = new ArrayList<TransformationFrame>();
		tac.rotation = new ArrayList<TransformationFrame>();
		tac.scale = new ArrayList<TransformationFrame>();
		
		tac.position.add(new TransformationFrame(0.0f, 0.0f, 0.0f, 0.0D));
		tac.position.add(new TransformationFrame(1.0f, 1.0f, 1.0f, 0.0D));
		
		tac.rotation.add(new TransformationFrame(0.0f, 0.0f, 0.0f, 0.0D));
		tac.rotation.add(new TransformationFrame(1.0f, 1.0f, 1.0f, 0.0D));
		
		tac.scale.add(new TransformationFrame(0.0f, 0.0f, 0.0f, 0.0D));
		tac.scale.add(new TransformationFrame(1.0f, 1.0f, 1.0f, 0.0D));
		Gson gson = new Gson();
		String s = gson.toJson(tac);
		System.out.println(s);*/
		String file = "res/animation/test";
		AnimationTransformation anim = AnimationLoader.loadTransformationAnimation(file);
		System.out.println(anim);
	}
}
