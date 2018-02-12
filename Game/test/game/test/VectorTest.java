package game.test;

import org.junit.Assert;
import org.junit.Test;

import caesar.util.Vector3f;

public class VectorTest {

	@Test
	public void testVectorDistance(){
		Vector3f v0 = new Vector3f();
		Vector3f v1 = new Vector3f(1.0f, 1.0f, 1.0f);
		float distance = v0.distanceTo(v1);
		System.out.println(distance + "," + Math.sqrt(3.0));
		Assert.assertEquals((float)Math.sqrt(3.0), distance, 0.0001);
	}
}
