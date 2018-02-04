package converter.model;

import java.util.ArrayList;
import java.util.List;

public class RawModel {

	private List<RawFace> faces = new ArrayList<RawFace>();
	
	public RawModel(){}
	
	public void addFace(RawFace face){
		faces.add(face);
	}
	
	public int getVertexCount(){
		return faces.size() * faces.get(0).size();
	}
	
	public List<RawFace> getFaces(){
		return faces;
	}
}
