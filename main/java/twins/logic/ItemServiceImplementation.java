package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.ItemEntity;

@Service
public class ItemServiceImplementation implements AdvancedItemService {
	private ItemDAO itemDAO;
	private ObjectMapper jackson;
	private String space;
	private AdvancedUserService userLogic;

	@Autowired
	public ItemServiceImplementation(ItemDAO itemDAO, AdvancedUserService userLogic) {
		super();
		this.itemDAO = itemDAO;
		this.jackson = new ObjectMapper();
		this.userLogic = userLogic;
	}

	// Read space name from Spring configuration property file
	@Value("${spring.application.name:CollegeFinance}")
	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	@Transactional
	public ItemBoundary createItem(ItemBoundary item) {
		UserBoundary result = login(item.getCreatedBy().getUserId().getSpace(), item.getCreatedBy().getUserId().getEmail());
		if (result == null || !result.getRole().equals("MANAGER")) {
			throw new RuntimeException("ItemServiceImplementation: createitem: User has no permission to Create new Item!");
		}
		
//		UserBoundary itemUser = new UserBoundary();
//		itemUser.setAvatar("default.jpg");
//		if(item.getType().equals("worker"))
//			itemUser.setRole("worker");
//		else
//			itemUser.setRole("player");
//
//		itemUser.setUsername(item.getName());
//		itemUser.setUserId();
		if (!isBoundaryValid(item))
			throw new RuntimeException("ItemServiceImplementation: createItem: Item Boundary is not Valid");
		ItemEntity entity = convertFromBoundary(item);
		entity.setCreatedTimestamp(new Date());
		entity.setItemspace(this.space);
		entity.setItemid(UUID.randomUUID().toString());
		entity.setUniqueId(entity.getItemspace()+":"+entity.getItemid());
		this.itemDAO.save(entity);
		return this.convertToBoundary(entity);
	}

	@Override
	@Transactional
	public ItemBoundary updateItem(ItemBoundary item) {
		UserBoundary result = login(item.getCreatedBy().getUserId().getSpace(), item.getCreatedBy().getUserId().getEmail());
		if (result == null || !result.getRole().equals("MANAGER")) {
			throw new RuntimeException("ItemServiceImplementation: updateItem: User has no permission to Create new Item!");
		}
		if (!isBoundaryValid(item))
			throw new MessageNotFoundException("ItemServiceImplementation: updateItem: Item is undefined");
		Optional<ItemEntity> op = Optional.of(itemDAO.findByItemid(item.getItemId().getId()));
		if (op.isPresent()) {
			ItemEntity entity = op.get();
			ItemEntity update = this.convertFromBoundary(item);
			update.setItemid(entity.getItemid());
			update.setItemspace(this.space);
			update.setCreatedTimestamp(entity.getCreatedTimestamp());
			update.setUniqueId(entity.getItemspace()+":"+entity.getItemid());
			this.itemDAO.save(update);
			return this.convertToBoundary(update);
		} else {
			throw new MessageNotFoundException("ItemServiceImplementation: updateItem: No items found for this user");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		if (!isBoundaryValidAdmin(userSpace, userEmail) || itemSpace == null || itemId == null)
			throw new MessageNotFoundException("ItemServiceImplementation:: getItem: User or item is undefined");
		Optional<ItemEntity> op = Optional.of(itemDAO.findByItemid(itemId));
		if (op.isPresent()) {
				return this.convertToBoundary(op.get());
		} else {
			throw new MessageNotFoundException("ItemServiceImplementation: getItem: No items found for this user");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public ItemBoundary getActiveItem(String userSpace, String userEmail, String itemSpace, String itemId) {
		if (!isBoundaryValidAdmin(userSpace, userEmail) || itemSpace == null || itemId == null)
			throw new MessageNotFoundException("ItemServiceImplementation: getActiveItem: User or item is undefined");
		Optional<ItemEntity> op = Optional.of(this.itemDAO.findByItemidAndItemspaceAndActive(itemId, itemSpace, true));
		if (op.isPresent()) {
				return this.convertToBoundary(op.get());
		} else {
			throw new MessageNotFoundException("ItemServiceImplementation: getActiveItem: No items found for this user");
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String adminSpace, String adminEmail) {
//		Iterable<ItemEntity> allEntities = this.itemDAO.findAll();
//		List<ItemBoundary> rv = new ArrayList<>();
//		for (ItemEntity entity : allEntities) {
//			ItemBoundary boundary = convertToBoundary(entity);
//			rv.add(boundary);
//		}
//		return rv;
		throw new RuntimeException("Deprecated operation - use the new API with (size, page)");
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<ItemBoundary> getAllItems(String adminSpace, String adminEmail, int size, int page) {
		Page<ItemEntity> pageOfEntities = this.itemDAO
				.findAll(PageRequest.of(page, size));
		
		List<ItemEntity> entities = pageOfEntities.getContent();
		List<ItemBoundary> rv = new ArrayList<>();
		for(ItemEntity entity: entities) {
			ItemBoundary boundary = convertToBoundary(entity);
			
			rv.add(boundary);
		}
		
		return rv;
	}
	
	@Override
	public List<ItemBoundary> getAllActiveItems(String adminSpace, String adminEmail, int size, int page) {
		List<ItemEntity> pageOfEntities = this.itemDAO
				.findAllByActive(true, PageRequest.of(page, size, Direction.ASC, "Itemid", "createdTimestamp"));

		List<ItemBoundary> rv = new ArrayList<>();
		for(ItemEntity entity: pageOfEntities) {
			ItemBoundary boundary = convertToBoundary(entity);
			rv.add(boundary);
		}
		return rv;
	}
	
	
	@Override
	public ItemBoundary getItemForUser(String userSpace, String userEmail, String itemSpace, String itemId) {
		UserBoundary result = login(userSpace, userEmail);
		if (result == null || result.getRole().equals("ADMIN")) {
			throw new RuntimeException("ItemServiceImplementation: getItemForUser: User has no permission to Get this Item!");
		}
		ItemBoundary item = new ItemBoundary();
		if(result.getRole().equals("MANAGER"))
			item = getItem(userSpace, userEmail, itemSpace, itemId);
		if(result.getRole().equals("PLAYER"))
			item = getActiveItem(userSpace, userEmail, itemSpace, itemId);
		
		return item;
	}
	
	@Override
	public ArrayList<ItemBoundary> getAllItemsForUser(String userSpace, String userEmail, int size, int page) {
		
		UserBoundary result = login(userSpace, userEmail);
		if (result == null || result.getRole().equals("ADMIN")) {
			throw new RuntimeException("ItemServiceImplementation: getAllItemsForUser: User has no permission to Get this item!");
		}
		ArrayList<ItemBoundary> itemByUser = new ArrayList<>();
		if(result.getRole().equals("MANAGER")) {
			itemByUser = (ArrayList<ItemBoundary>) getAllItems(userSpace,
					userEmail, size, page); // check it
		} else if (result.getRole().equals("PLAYER")) {
			itemByUser = (ArrayList<ItemBoundary>) getAllActiveItems(userSpace,
					userEmail, size, page); // check it
		}
		return itemByUser;
	}


	@Override
	@Transactional
	public void deleteAllItems(String adminSpace, String adminEmail) {
		UserBoundary result = login(adminSpace, adminEmail);
		if (result == null || !result.getRole().equals("ADMIN")) {
			throw new RuntimeException("ItemServiceImplementation: deleteAllItems: User has no permission to delete item!");
		}
		this.itemDAO.deleteAll();

	}

	private ItemEntity convertFromBoundary(ItemBoundary boundary) {
		ItemEntity entity = new ItemEntity();
		entity.setItemid(boundary.getItemId().getId());
		entity.setItemspace(boundary.getItemId().getSpace());
		entity.setUniqueId(entity.getItemspace()+":"+entity.getItemid());
		entity.setType(boundary.getType());
		entity.setName(boundary.getName());
		entity.setActive(boundary.isActive());
		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());
		entity.setCreatedBy(boundary.getCreatedBy().getUserId().getEmail());
		String location = this.marshal(boundary.getLocation());
		entity.setLocation(location);
		String itemAttributes = this.marshal(boundary.getItemAttributes());
		entity.setItemAttributes(itemAttributes);
		// TODO add Item and invokedBy

		return entity;
	}

	private ItemBoundary convertToBoundary(ItemEntity entity) {
		ItemBoundary boundary = new ItemBoundary();
		boundary.setItemId(new ItemId(entity.getItemid(), entity.getItemspace()));
		boundary.setType(entity.getType());
		boundary.setName(entity.getName());
		boundary.setActive(entity.getActive());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setCreatedBy(new User(entity.getCreatedBy(), this.space));
		boundary.setItemAttributes(this.unmarshal(entity.getItemAttributes(), Map.class));
		boundary.setLocation(this.unmarshal(entity.getLocation(), Map.class));
		return boundary;
	}

	// use Jackson to convert JSON to Object
	private <T> T unmarshal(String json, Class<T> type) {
		try {
			return this.jackson.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String marshal(Object moreDetails) {
		try {
			return this.jackson.writeValueAsString(moreDetails);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isBoundaryValidAdmin(String adminSpace, String adminEmail) {
		if (adminSpace == null)
			return false;

		if (adminEmail == null)
			return false;

		return true;
	}

	public boolean isBoundaryValid(ItemBoundary input) {
		if (input.getItemId() == null)
			return false;

		if (input.getType() == null)
			return false;

		return true;
	}
	
	public UserBoundary login(String userSpace, String userEmail) {
		UserBoundary op = this.userLogic.login(userSpace, userEmail);
		if (op != null) {
			return op;
		} else {
			throw new MessageNotFoundException("ItemServiceImplementation: login: no user in database");
		}
	}




}