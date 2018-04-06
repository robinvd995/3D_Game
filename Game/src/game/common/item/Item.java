package game.common.item;

public class Item {

	public static final int ITEM_AMOUNT = 3;

	private static final Item[] ITEMS = new Item[ITEM_AMOUNT];

	public static final Item PICKAXE = new Item(0);
	public static final Item SWORD = new Item(1);
	public static final Item BOW = new Item(2);
	
	private final int itemId;

	public Item(int itemId){
		this.itemId = itemId;
		ITEMS[itemId] = this;
	}

	public int getItemId(){
		return itemId;
	}
}
