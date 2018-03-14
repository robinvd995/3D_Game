package game.test;

import java.nio.IntBuffer;

import org.junit.Assert;
import org.junit.Test;
import org.lwjgl.BufferUtils;


public class BufferTest {

	@Test
	public void bufferTest() {
		int[] test = {0,1,2,3,4,5,6,7,8,9,10};
		IntBuffer buffer = BufferUtils.createIntBuffer(test.length);
		buffer.put(test);
		int l = test.length;
		int limit = buffer.limit();
		System.out.println(l + "," + limit);
		Assert.assertEquals("Message", test.length, buffer.limit());
	}

}
