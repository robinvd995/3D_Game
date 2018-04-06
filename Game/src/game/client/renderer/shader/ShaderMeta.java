package game.client.renderer.shader;

public class ShaderMeta {

	private String version;
	
	private String vertex;
	private String fragment;
	
	public String getVersion(){
		return version;
	}
	
	public String getVertexFile(){
		return vertex;
	}
	
	public String getFragmentFile(){
		return fragment;
	}
}
