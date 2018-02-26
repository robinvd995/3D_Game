package game.test;

import org.junit.Assert;
import org.junit.Test;

import caesar.util.Vector3f;
import game.common.physics.AxisAlignedBB;
import game.common.physics.Ray;
import game.common.physics.RayResult;
import game.common.util.EnumDirection;

public class TestRayIntersection {

	@Test
	public void testRayIntersection(){
		Vector3f rayOrigin = new Vector3f(1.0f, 0.0f, -1.0f);
        Vector3f rayDest = new Vector3f(-2.0f, 0.0f,  1.0f);

        Ray ray = new Ray(rayOrigin, rayDest);
        
        Vector3f s1 = new Vector3f(-1.0f, 1.0f, 0.0f);
        Vector3f s2 = new Vector3f( 1.0f, 1.0f, 0.0f);
        Vector3f s3 = new Vector3f(-1.0f,-1.0f, 0.0f);

        RayResult result = ray.intersectRayWithSquare(s1, s2, s3);
        assert result.intersects();

        rayOrigin = new Vector3f(1.5f, 1.5f, -1.0f);
        rayDest = new Vector3f(1.5f, 1.5f,  1.0f);
        ray = new Ray(rayOrigin, rayDest);
        
        result = ray.intersectRayWithSquare(s1, s2, s3);
        assert !result.intersects();
	}

	@Test
	public void testRayIntersectionWithAABBFront(){
		Vector3f rayOrigin = new Vector3f(0.0f, 0.0f, -10.0f);
		Vector3f rayDest = new Vector3f(0.0f, 0.0f, 10.0f);
		Vector3f intersectionPoint = new Vector3f(0.0f, 0.0f, -0.5f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.FRONT);
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
	
	@Test
	public void testRayIntersectionWithAABBBack(){
		Vector3f rayOrigin = new Vector3f(0.0f, 0.0f, 10.0f);
		Vector3f rayDest = new Vector3f(0.0f, 0.0f, -10.0f);
		Vector3f intersectionPoint = new Vector3f(0.0f, 0.0f, 0.5f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.BACK);
		
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
	
	@Test
	public void testRayIntersectionWithAABBLeft(){
		Vector3f rayOrigin = new Vector3f(-10.0f, 0.0f, 0.0f);
		Vector3f rayDest = new Vector3f(10.0f, 0.0f, 0.0f);
		Vector3f intersectionPoint = new Vector3f(-0.5f, 0.0f, 0.0f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.LEFT);
		
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
	
	@Test
	public void testRayIntersectionWithAABBRight(){
		Vector3f rayOrigin = new Vector3f(10.0f, 0.0f, 0.0f);
		Vector3f rayDest = new Vector3f(-10.0f, 0.0f, 0.0f);
		Vector3f intersectionPoint = new Vector3f(0.5f, 0.0f, 0.0f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.RIGHT);
		
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
	
	@Test
	public void testRayIntersectionWithAABBUp(){
		Vector3f rayOrigin = new Vector3f(0.0f, 10.0f, 0.0f);
		Vector3f rayDest = new Vector3f(0.0f, -10.0f, 0.0f);
		Vector3f intersectionPoint = new Vector3f(0.0f, 0.5f, 0.0f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.UP);
		
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
	
	@Test
	public void testRayIntersectionWithAABBDown(){
		Vector3f rayOrigin = new Vector3f(0.0f, -10.0f, 0.0f);
		Vector3f rayDest = new Vector3f(0.0f, 10.0f, 0.0f);
		Vector3f intersectionPoint = new Vector3f(0.0f, -0.5f, 0.0f);
		
		AxisAlignedBB aabb = new AxisAlignedBB(-0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
		Vector3f pos = new Vector3f();
		
		Ray ray = new Ray(rayOrigin, rayDest);
		RayResult result = ray.intersectRayWithAABBFace(aabb, pos, EnumDirection.DOWN);
		
		Assert.assertTrue(result.intersects());
		Assert.assertEquals(intersectionPoint, result.getPoint());
	}
}
