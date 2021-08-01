package twins.logic;

public class Item {

	private ItemId itemId;
	
	public Item() {
	}

	public Item(String id,String space) {
		this.itemId = new ItemId(id,space);
	}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + "]";
	}

}
