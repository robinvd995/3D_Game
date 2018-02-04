package converter.api.model;

public class Face {

	private int vert0, vert1, vert2;
	
	public Face(){
		vert0 = -1;
		vert1 = -1;
		vert2 = -1;
	}
	
	private Face(int vert0, int vert1, int vert2) {
		this.vert0 = vert0;
		this.vert1 = vert1;
		this.vert2 = vert2;
	}

	public int getVert0(){
		return vert0;
	}
	
	public int getVert1(){
		return vert1;
	}
	
	public int getVert2(){
		return vert2;
	}
	
	public boolean isValid(){
		return vert0 >= 0 && vert1 >= 0 && vert2 >= 0;
	}
	
	public void setNext(int index){
		if(vert0 == -1){
			vert0 = index;
			return;
		}
		if(vert1 == -1){
			vert1 = index;
			return;
		}
		if(vert2 == -1){
			vert2 = index;
			return;
		}
		throw new IllegalArgumentException("Face has no more empty slots!");
	}
	
	public Face copy(){
		return new Face(vert0, vert1, vert2);
	}

	@Override
	public String toString() {
		return "Face [vert0=" + vert0 + ", vert1=" + vert1 + ", vert2=" + vert2 + "]";
	}
	
	
}
