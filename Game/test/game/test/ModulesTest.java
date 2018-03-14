package game.test;

import org.junit.Test;

public class ModulesTest {

	@Test
	public void test(){
		int i0 = -32;
		int i1 = -29;
		int i2 = 16;
		int i3 = 19;
		
		assert i1 - i0 == 3;
		assert i3 - i2 == 3;
		
		System.out.println(Math.floor((double)i3 / (double)16));
	}
}
