package twins.logic;


/* This class represent a ItemId in the system.
 * space - The space the user exist, given by server.
 * id - The Id of the item, unique to each space.
 */
public class ItemId {

	
	private String space;
	private String id;
	
	public ItemId() {
	}
	public ItemId(String id,String space) {
		this.id = id;
		this.space = space;
	}
	
	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ItemId [space=" + space + ", id=" + id + "]";
	}

}
