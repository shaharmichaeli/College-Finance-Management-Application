package twins.logic;

import java.util.ArrayList;
import java.util.List;

public interface AdvancedItemService extends ItemService {
	
	public List<ItemBoundary> getAllItems(String adminSpace, String adminEmail, int size, int page);
	
	public List<ItemBoundary> getAllActiveItems(String adminSpace, String adminEmail, int size, int page);
	
	public ItemBoundary getActiveItem(String userSpace, String userEmail, String itemSpace, String itemId);
	
	public ItemBoundary getItemForUser(String userSpace, String userEmail, String itemSpace, String itemId);
	
	public ArrayList<ItemBoundary> getAllItemsForUser(String userSpace, String userEmail, int size, int page);


}
