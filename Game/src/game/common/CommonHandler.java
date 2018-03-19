package game.common;

public class CommonHandler {

	private static CommonHandler handler;

	private EnumSide side;
	
	private CommonHandler(EnumSide side){
		this.side = side;
	}
	
	public EnumSide getSide() {
		return side;
	}
	
	public static CommonHandler instance(){
		return handler;
	}
	
	public static final void init(EnumSide side){
		if(handler == null){
			handler = new CommonHandler(side);
		}
		else{
			throw new RuntimeException("Already instantiated a CommonHandler!");
		}
	}
}
