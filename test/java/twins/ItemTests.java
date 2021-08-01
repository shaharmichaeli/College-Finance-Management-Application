package twins;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import twins.logic.ItemBoundary;
import twins.logic.ItemId;
import twins.logic.NewUserDetails;
import twins.logic.User;
import twins.logic.UserBoundary;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ItemTests {
	private int port;
	private String url;
	private RestTemplate restTemplate;
	
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}
	
	@PostConstruct
	public void init() {
		this.url = "http://localhost:" + port + "/twins";
		this.restTemplate = new RestTemplate();
	}
	
	@AfterEach
	public void cleanUp() {
		UserBoundary ub = new UserBoundary();
		ub.setUserId("test@test.com", "dummy");
		ub.setUsername("test");
		ub.setRole("PLAYER");
		ub.setAvatar("test.jpg");
//		ItemBoundary item = new ItemBoundary(new ItemId(), "testType", "testItem", true, new User());
		this.restTemplate
			.delete(this.url + "/admin/items/{userSpace}/{userEmail}",
					ub.getUserId().getSpace(),
					ub.getUserId().getEmail());
	}
	
	
	
	@Test
	public void checkServerIsUpAndFunctional() {
	}
	
	@Test
	public void testUserCreateItemAndServerStoresTheData() throws Exception {
		//GIVEN the server up, do nothing
		
		//WHEN i POST /items/{userSpace}/{userEmail} and send item JSON
		User user = new User("test@test.com", "dummy");
		ItemBoundary newItem = new ItemBoundary(new ItemId("2", "dummy"), "testType", "testItem", true,
				new User(user.getUserId().getEmail(), user.getUserId().getSpace()));
		
		ItemBoundary response = this.restTemplate
				.postForObject(this.url + "/items/{userSpace}/{userEmail}", newItem, ItemBoundary.class,
						user.getUserId().getSpace(),
						user.getUserId().getEmail());
		
		
		//THEN the server stores the item in the database
		//invoke HTTP GET /items/{userSpace}/{userEmail}/{itemSpace}/{itemId}
		// assert the item retrieved is the item posted to the database
		
		ItemBoundary itemInDb = this.restTemplate
				.getForObject(this.url + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", ItemBoundary.class,
						response.getCreatedBy().getUserId().getSpace(),
						response.getCreatedBy().getUserId().getEmail(),
						response.getItemId().getSpace(),
						response.getItemId().getId());
		
		assertThat(itemInDb.getItemId().getSpace())
				.isEqualTo(response.getItemId().getSpace());
		
		assertThat(itemInDb.getItemId().getId())
				.isEqualTo(response.getItemId().getId());
	}
	
	@Test
	public void testItemUpdated() throws Exception {
		//GIVEN the server up
		//AND the server contains an item inside with content: 
		/* * {
			 * 	"itemId": {
			 * 		"space":"CollegeFinance"
			 * 		"id":"2"
			 * 	},
			 * 	"type":"demoType",
			 * 	"name":"demo item",
			 * 	"active":true,
			 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
			 * 	"createdBy":{
			 * 		"userId":{
			 * 			"space":"CollegeFinance",
			 * 			"email":"user@demo.com"
			 * 		}
			 * 	},
			 * 	"location":{
			 * 		"lat":32.115139,
			 * 		"lng":34.817804
			 * 	},
			 * "itemAttributes":{
			 * 		"key1":"can be set to any value you wish",
			 * 		"key2":"you can also name the attributes any name you like",
			 * 		"key3":58,
			 * 		"key4":false
			 * 	}
		* 	 }
		 */
		
		Map<String, Object> location =  new HashMap<>();
		location.put("lat", 32.115139);
		location.put("lng", 34.817804);
		
		Map<String, Object> itemAttributes =  new HashMap<>();
		itemAttributes.put("key1", "can be set to any value you wish");
		itemAttributes.put("key2", "you can also name the attributes any name you like");
		itemAttributes.put("key3", 58);
		itemAttributes.put("key4", false);

		
		ItemBoundary newItem = new ItemBoundary(new ItemId("2", "dummy"), "demoType", "demo item", true,
				new User("dummy", "user@demo.com"));
		newItem.setLocation(location);
		newItem.setItemAttributes(itemAttributes);
		
		ItemBoundary actual = this.restTemplate
				.postForObject(this.url + "/items/{userSpace}/{userEmail}", newItem, ItemBoundary.class,
						newItem.getCreatedBy().getUserId().getSpace(),
						newItem.getCreatedBy().getUserId().getEmail());
		
		//WHEN i invoke PUT /twins/items/{userSpace}/{userEmail}/{itemSpace}/{itemId} with item JSON 
		// changing name to changed item and active to false
		/*  {
			 * 	"name":"changed item",
			 * 	"active":false,
 * 			}
		 */
		actual.setActive(false);
		actual.setName("changed item");
		ItemBoundary updatedItem = actual;

		this.restTemplate
			.put(this.url + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", updatedItem,
					actual.getCreatedBy().getUserId().getSpace(), 
					actual.getCreatedBy().getUserId().getEmail(),
					actual.getItemId().getSpace(),
					actual.getItemId().getId());
			
		
		//THEN the database is updated
		
		ItemBoundary itemInDb = this.restTemplate
				.getForObject(this.url + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", ItemBoundary.class,
						actual.getCreatedBy().getUserId().getSpace(),
						actual.getCreatedBy().getUserId().getEmail(),
						actual.getItemId().getSpace(),
						actual.getItemId().getId());
		
		assertThat(itemInDb.getItemId().getId())
			.isEqualTo(updatedItem.getItemId().getId());
		
		assertThat(itemInDb.getItemId().getSpace())
		.isEqualTo(updatedItem.getItemId().getSpace());
		
	}
	
	
	@Test
	public void testGetAllItemsForUserFromDatabaseAndReturnsAllItems() throws Exception {
		//GIVEN the server up
		//AND the server contains an item inside with content: 
		/* * {
		 * 	"itemId": {
		 * 		"space":"CollegeFinance"
		 * 		"id":"2"
		 * 	},
		 * 	"type":"demoType",
		 * 	"name":"demo item",
		 * 	"active":true,
		 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
		 * 	"createdBy":{
		 * 		"userId":{
		 * 			"space":"CollegeFinance",
		 * 			"email":"user@demo.com"
		 * 		}
		 * 	},
		 * 	"location":{
		 * 		"lat":32.115139,
		 * 		"lng":34.817804
		 * 	},
		 * "itemAttributes":{
		 * 		"key1":"can be set to any value you wish",
		 * 		"key2":"you can also name the attributes any name you like",
		 * 		"key3":58,
		 * 		"key4":false
		 * 	}
	* 	 }
	 */
		
		Map<String, Object> location =  new HashMap<>();
		location.put("lat", 32.115139);
		location.put("lng", 34.817804);
		
		Map<String, Object> itemAttributes =  new HashMap<>();
		itemAttributes.put("key1", "can be set to any value you wish");
		itemAttributes.put("key2", "you can also name the attributes any name you like");
		itemAttributes.put("key3", 58);
		itemAttributes.put("key4", false);

		
		ItemBoundary newItem = new ItemBoundary(new ItemId("2", "dummy"), "demoType", "demo item", true,
				new User("dummy", "user@demo.com"));
		newItem.setLocation(location);
		newItem.setItemAttributes(itemAttributes);
		
		ItemBoundary actual = this.restTemplate
				.postForObject(this.url + "/items/{userSpace}/{userEmail}", newItem, ItemBoundary.class,
						newItem.getCreatedBy().getUserId().getSpace(),
						newItem.getCreatedBy().getUserId().getEmail());
		
		
		//WHEN i invoke GET /items/{userSpace}/{userEmail} Return all Items for specific User in database
		ItemBoundary[] result = this.restTemplate
				.getForObject(this.url + "/items/{userSpace}/{userEmail}", ItemBoundary[].class,
						actual.getCreatedBy().getUserId().getSpace(),
						actual.getCreatedBy().getUserId().getEmail());
			
		
		//THEN the database contains all the correct information

		assertThat(result)
			.isNotNull()
			.isNotEmpty();
	}
	
	@Test
	public void testGetSpecificItem() throws Exception {
		//GIVEN the server up
		//AND the server contains an item inside with content: 
		/* * {
		 * 	"itemId": {
		 * 		"space":"CollegeFinance"
		 * 		"id":"2"
		 * 	},
		 * 	"type":"demoType",
		 * 	"name":"demo item",
		 * 	"active":true,
		 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
		 * 	"createdBy":{
		 * 		"userId":{
		 * 			"space":"CollegeFinance",
		 * 			"email":"user@demo.com"
		 * 		}
		 * 	},
		 * 	"location":{
		 * 		"lat":32.115139,
		 * 		"lng":34.817804
		 * 	},
		 * "itemAttributes":{
		 * 		"key1":"can be set to any value you wish",
		 * 		"key2":"you can also name the attributes any name you like",
		 * 		"key3":58,
		 * 		"key4":false
		 * 	}
	* 	 }
	 */
		
		Map<String, Object> location =  new HashMap<>();
		location.put("lat", 32.115139);
		location.put("lng", 34.817804);
		
		Map<String, Object> itemAttributes =  new HashMap<>();
		itemAttributes.put("key1", "can be set to any value you wish");
		itemAttributes.put("key2", "you can also name the attributes any name you like");
		itemAttributes.put("key3", 58);
		itemAttributes.put("key4", false);

		
		ItemBoundary newItem = new ItemBoundary(new ItemId("2", "dummy"), "demoType", "demo item", true,
				new User("dummy", "user@demo.com"));
		newItem.setLocation(location);
		newItem.setItemAttributes(itemAttributes);
		
		ItemBoundary actual = this.restTemplate
				.postForObject(this.url + "/items/{userSpace}/{userEmail}", newItem, ItemBoundary.class,
						newItem.getCreatedBy().getUserId().getSpace(),
						newItem.getCreatedBy().getUserId().getEmail());
		
		
		//WHEN i invoke GET /items/{userSpace}/{userEmail}/{itemSpace}/{itemId} Return a specific Item from database
		ItemBoundary result = this.restTemplate
				.getForObject(this.url + "/items/{userSpace}/{userEmail}/{itemSpace}/{itemId}", ItemBoundary.class,
						actual.getCreatedBy().getUserId().getSpace(),
						actual.getCreatedBy().getUserId().getEmail(),
						actual.getItemId().getSpace(),
						actual.getItemId().getId());
			
		
		//THEN the database contains the specific item

		assertThat(result)
			.isNotNull();
	}
	
	
	
	
}
