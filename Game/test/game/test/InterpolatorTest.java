package game.test;

import org.junit.Assert;
import org.junit.Test;

import caesar.util.interpolate.IInterpolatable;
import caesar.util.interpolate.InterpolateFunctions;
import caesar.util.interpolate.Interpolator;

public class InterpolatorTest {

	@Test
	public void interpolatorIndexTest(){
		Interpolator<FloatWrapper> interpolator = new Interpolator<FloatWrapper>(5.0f);
		interpolator.addInterpolation(1.0f, new FloatWrapper(0.0f));
		interpolator.addInterpolation(2.0f, new FloatWrapper(1.0f));
		interpolator.addInterpolation(3.0f, new FloatWrapper(0.0f));
		interpolator.addInterpolation(4.0f, new FloatWrapper(0.0f));
		
		int index0 = interpolator.calcCurrentIndex(0.5f);
		int index1 = interpolator.calcCurrentIndex(1.5f);
		int index2 = interpolator.calcCurrentIndex(2.5f);
		int index3 = interpolator.calcCurrentIndex(3.5f);
		int index4 = interpolator.calcCurrentIndex(4.5f);
		
		//System.out.println("Index0 = " + index0);
		//System.out.println("Index1 = " + index1);
		//System.out.println("Index2 = " + index2);
		//System.out.println("Index3 = " + index3);
		//System.out.println("Index4 = " + index4);
		
		Assert.assertEquals(3, index0);
		Assert.assertEquals(0, index1);
		Assert.assertEquals(1, index2);
		Assert.assertEquals(2, index3);
		Assert.assertEquals(3, index4);
	}
	
	@Test
	public void interpolatorValueTest(){
		Interpolator<FloatWrapper> interpolator = new Interpolator<FloatWrapper>(2.0f);
		interpolator.addInterpolation(0.0f, new FloatWrapper(0.0f));
		interpolator.addInterpolation(1.0f, new FloatWrapper(1.0f));
		
		float value0 = interpolator.getInterpolatedValue(0.5f).value;
		float value1 = interpolator.getInterpolatedValue(1.25f).value;
		
		//System.out.println("value0 = " + value0);
		//System.out.println("value1 = " + value1);
		
		Assert.assertEquals(0.5f, value0, 0.01f);
		Assert.assertEquals(0.75f, value1, 0.01f);
	}
	
	@Test
	public void interpolatorSmoothstepTest(){
		Interpolator<FloatWrapper> interpolator = new Interpolator<FloatWrapper>(2.0f, InterpolateFunctions.FUNCTION_SMOOTHSTEP);
		interpolator.addInterpolation(0.0f, new FloatWrapper(0.0f));
		interpolator.addInterpolation(1.0f, new FloatWrapper(1.0f));
		System.out.println(interpolator.getInterpolatedValue(0.1f));
		System.out.println(interpolator.getInterpolatedValue(0.3f));
		System.out.println(interpolator.getInterpolatedValue(0.5f));
		System.out.println(interpolator.getInterpolatedValue(0.7f));
		System.out.println(interpolator.getInterpolatedValue(0.9f));
	}
	
	private static class FloatWrapper implements IInterpolatable<FloatWrapper> {

		private float value;
		
		public FloatWrapper(float value){
			this.value = value;
		}
		
		@Override
		public FloatWrapper interpolate(double factor, FloatWrapper other) {
			//System.out.println("Factor:" + factor);
			float delta = other.value - value;
			return new FloatWrapper((float) (value + (factor * delta)));
		}
		
		@Override
		public String toString(){
			return "FloatWrapper + [value=" + value + "]";
		}
	}
}
