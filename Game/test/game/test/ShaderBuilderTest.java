package game.test;

import org.junit.Test;

import game.client.renderer.shader.ShaderBuilder;

public class ShaderBuilderTest {

	@Test
	public void testShaderBuilder(){
		ShaderBuilder.newInstance("entity").buildShader();
	}
}
