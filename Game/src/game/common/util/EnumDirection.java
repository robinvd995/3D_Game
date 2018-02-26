package game.common.util;

public enum EnumDirection {

	NONE(-1, 0, 0, 0, "undefined"), 
	UP(0, 0, 1, 0, "up"),
	DOWN(1, 0, -1, 0, "down"),
	LEFT(2, 1, 0, 0, "left"),
	RIGHT(3, -1, 0, 0, "right"),
	FRONT(4, 0, 0, 1, "front"),
	BACK(5, 0, 0, -1, "back");
	
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
	
	private final int directionId;
	private final int offX;
	private final int offY;
	private final int offZ;
	private final String unlocalizedName;
	
	private EnumDirection(int dirId, int offX, int offY, int offZ, String name){
		this.directionId = dirId;
		this.offX = offX;
		this.offY = offY;
		this.offZ = offZ;
		this.unlocalizedName = name;
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
	
	public int getDirectionId(){
		return directionId;
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
		default: return NONE;
		}
	}
	
	public static EnumDirection getDirectionFromId(int directionId){
		return directionId >= 0 && directionId < 6 ? VALID_DIRECTIONS[directionId] : NONE;
	}
	
	public String getUnlocalizedName(){
		return unlocalizedName;
	}
}
