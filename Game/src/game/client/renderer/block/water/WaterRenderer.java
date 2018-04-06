package game.client.renderer.block.water;

public class WaterRenderer {

	private WaterFrameBuffer frameBuffer;
	
	public WaterRenderer(){}
	
	public void initRenderer(){
		frameBuffer = WaterFrameBuffer.createFrameBuffer();
	}
	
	public WaterFrameBuffer getBuffer(){
		return frameBuffer;
	}
}
