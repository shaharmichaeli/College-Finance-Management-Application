package twins.logic;

import java.util.List;

public interface ItemService {
	public ItemBoundary createItem (ItemBoundary item);

	public ItemBoundary updateItem(ItemBoundary item);
	
@Deprecated	public List<ItemBoundary> getAllItems(String adminSpace, String adminEmail);

	public void deleteAllItems(String adminSpace, String adminEmail);

	public ItemBoundary getItem(String userSpace, String userEmail, String itemSpace, String itemId);
}
