package game.client.renderer.texture;

public class BlockTextureData {
	
	private String diffuse = "";
	private String specular = "";
	private String normal = "";
	
	public BlockTextureData(){}
	
	public String getDiffuseMap(){
		return diffuse;
	}
	
	public String getSpecularMap(){
		return specular;
	}
	
	public String getNormalMap(){
		return normal;
	}
	
	public boolean equals(Object other){
		return other instanceof BlockTextureData ? ((BlockTextureData)other).diffuse.equals(diffuse) : false;
	}
	
	/*public void setTextureId(String id){
		this.textureId = id;
	}
	
	public String getTextureId(){
		return textureId;
	}*/
}
