package game.common.inventory;

import game.common.item.Item;

public class Inventory {

	private final Item[] inventory;
	
	public Inventory(int size){
		inventory = new Item[size];
	}
	
	/**
	 * Gets the item at the given index of this inventory
	 * @param index the position of the item in this inventory
	 * @return the item in the slot of the given index, null if there is no item
	 */
	public Item getItemAtIndex(int index){
		return inventory[index];
	}
	
	/**
	 * Adds a item in the first available slot in this inventory
	 * @param item the item to add
	 * @return true if the item was added, false otherwise
	 */
	public boolean addItem(Item item){
		for(int i = 0; i < inventory.length; i++){
			if(inventory[i] == null){
				inventory[i] = item;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Places the given item into a slot of this inventory
	 * @param index the slot index
	 * @param item the item to set
	 */
	public void setItem(int index, Item item){
		inventory[index] = item;
	}
	
	/**
	 * Removes an item at the given index of this inventory
	 * @param index the index of the item to be removed
	 */
	public void removeItem(int index){
		inventory[index] = null;
	}
	
	/**
	 * Checks whether or not the item exists in this inventory
	 * @param item the item to check
	 * @return true if the item exists, false otherwise
	 */
	public boolean hasItem(Item item){
		for(int i = 0; i < inventory.length; i++){
			if(inventory[i] != null && inventory[i].getItemId() == item.getItemId())
				return true;
		}
		return false;
	}
	
	/**
	 * Gets the index of the first occurance of the given item
	 * @param item the item to look for
	 * @return the index of the given item, if no item is found -1 is returned
	 */
	public int getIndexOf(Item item){
		for(int i = 0; i < inventory.length; i++){
			if(inventory[i] == item) return i;
		}
		return -1;
	}
	
	/**
	 * Retrieves the size of this inventory
	 * @return the size of this inventory
	 */
	public int getSize(){
		return inventory.length;
	}
}
