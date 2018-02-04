package converter.api.model;

public class ModelSection {

	private int start;
	private int count;
	
	public ModelSection(int start, int count){
		this.start = start;
		this.count = count;
	}
	
	public int getStart(){
		return this.start;
	}
	
	public int getCount(){
		return this.count;
	}
}
