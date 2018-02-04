package game.util;

public enum EnumDirection {

	UP(0, 1, 0),
	DOWN(0, -1, 0),
	LEFT(-1, 0, 0),
	RIGHT(1, 0, 0),
	FRONT(0, 0, -1),
	BACK(0, 0, 1);
	
	private static final EnumDirection[] VALID_DIRECTIONS = new EnumDirection[6];
	private static final EnumDirection[] HORIZONTAL_DIRECTIONS = new EnumDirection[4];
	
	static{
		VALID_DIRECTIONS[0] = UP;
		VALID_DIRECTIONS[1] = DOWN;
		VALID_DIRECTIONS[2] = LEFT;
		VALID_DIRECTIONS[3] = RIGHT;
		VALID_DIRECTIONS[4] = FRONT;
		VALID_DIRECTIONS[5] = BACK;
		
		HORIZONTAL_DIRECTIONS[0] = LEFT;
		HORIZONTAL_DIRECTIONS[1] = RIGHT;
		HORIZONTAL_DIRECTIONS[2] = FRONT;
		HORIZONTAL_DIRECTIONS[3] = BACK;
	}
	
	private int offX;
	private int offY;
	private int offZ;
	
	private EnumDirection(int offX, int offY, int offZ){
		this.offX = offX;
		this.offY = offY;
		this.offZ = offZ;
	}
	
	public int getOffsetX(){
		return offX;
	}
	
	public int getOffsetY(){
		return offY;
	}
	
	public int getOffsetZ(){
		return offZ;
	}
	
	public static EnumDirection[] getValidDirections(){
		return VALID_DIRECTIONS;
	}
	
	public static EnumDirection[] getHorizontalDirections(){
		return HORIZONTAL_DIRECTIONS;
	}
	
	public EnumDirection opposite(){
		switch(this){
		case UP: return DOWN;
		case DOWN: return UP;
		case LEFT: return RIGHT;
		case RIGHT: return LEFT;
		case FRONT: return BACK;
		case BACK: return FRONT;
		}
		return null;
	}
}
