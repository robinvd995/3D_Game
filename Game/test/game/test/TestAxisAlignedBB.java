package game.test;

import org.junit.Assert;
import org.junit.Test;

import caesar.util.Vector3f;
import game.physics.AxisAlignedBB;

public class TestAxisAlignedBB {

	@Test
	public void testAxisAlignedBB() {
		AxisAlignedBB aabb0 = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		AxisAlignedBB aabb1 = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		
		Vector3f myPos = new Vector3f();
		Vector3f otherPos = new Vector3f(1.0f, 0.0f, 0.0f);
		
		Assert.assertTrue(aabb0.intersect(aabb1, myPos, otherPos));
	}

}
