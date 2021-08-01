package twins.logic;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import twins.data.UserEntity;

@CrossOrigin
@RestController
public class ItemController {
	private AdvancedItemService itemService;
	private AdvancedUserService userLogic;

	ArrayList<ItemBoundary> allItems = new ArrayList<ItemBoundary>();

	@Autowired
	public void ItemServiceImplementation(AdvancedItemService itemService) {
		this.itemService = itemService;
	}

	@Autowired
	public void UserServiceImplementation(AdvancedUserService userLogic) {
		this.userLogic = userLogic;
	}

	// Create new item
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary createNewItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail, @RequestBody ItemBoundary inputItem) {
		inputItem.setCreatedBy(new User(userEmail, userSpace));
		return this.itemService.createItem(inputItem);
	}

	// Update an item
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary updateItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail, @PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId, @RequestBody ItemBoundary newItemInfo) {
		return this.itemService.updateItem(newItemInfo);
	}

	// Retrieve specific Item
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ItemBoundary getItem(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail, @PathVariable("itemSpace") String itemSpace,
			@PathVariable("itemId") String itemId) {

		return this.itemService.getItemForUser(userSpace, userEmail, itemSpace, itemId);
	}

	// Get All Items
	@RequestMapping(path = "/twins/items/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

	public ArrayList<ItemBoundary> getAllItemsForSpecificUser(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam (name="size", required = false, defaultValue = "10") int size,
			@RequestParam (name="page", required = false, defaultValue = "0") int page) {
		
		return this.itemService.getAllItemsForUser(userSpace,userEmail, size, page);
	}

	// DELETE all Items for specific user
	@RequestMapping(path = "/twins/admin/items/{userSpace}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteAllItems(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		this.itemService.deleteAllItems(userSpace, userEmail);
	}

	public UserBoundary login(String userSpace, String userEmail) {
		UserBoundary ub = this.userLogic.login(userSpace, userEmail);
		if (ub != null) {
			return ub;
		} else {
			throw new MessageNotFoundException("User login: user dont in database");
		}
	}
}
