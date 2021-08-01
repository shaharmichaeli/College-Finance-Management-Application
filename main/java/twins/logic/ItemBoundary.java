package twins.logic;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;


/*
 * This class is used to transfer item data. 
 * JSON Example:
 * {
 * 	"itemId": {
 * 		"space":"2021b.twins.afeka"
 * 		"id":"99"
 * 	},
 * 	"type":"demoType",
 * 	"name":"demo item",
 * 	"active":true,
 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
 * 	"createdBy":{
 * 		"userId":{
 * 			"space":"2021b.twins",
 * 			"email":"user2@demo.com"
 * 		}
 * 	},
 * 	"location":{
 * 		"lat":32.115139,
 * 		"lng":34.817804
 * 	},
 * "itemAttributes":{
 * 		"User":"
 * 				"userID": {
 * 					"space" : "",
 * 					"email" : "",
 * 	 			}",
 * 		"key2":"you can also name the attributes any name you like",
 * 		"key3":58,
 * 		"key4":false
 * 	}
 * }
 */
/* 
 * This JSON is an example of Student item:
 * {
 * 	"itemId": {
 * 		"space":"2021b.twins.afeka"
 * 		"id":"99"
 * 	},
 * 	"type":"Student",
 * 	"name":"User.username",
 * 	"active":true,
 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
 * 	"createdBy":{
 * 		"userId":{
 * 			"space":"2021b.twins",
 * 			"email":"user2@demo.com"
 * 		}
 * 	},
 * 	"location":{
 * 		"lat":32.115139,
 * 		"lng":34.817804
 * 	},
 * "itemAttributes":{
 * 		"User": "space" : "email",
 * 		"Balance":"110"
 * 	}
 * }
 */

/* 
 * This JSON is an example of Worker item:
 * {
 * 	"itemId": {
 * 		"space":"2021b.twins.afeka"
 * 		"id":"99"
 * 	},
 * 	"type":"Worker",
 * 	"name":"User.username",
 * 	"active":true,
 * 	"createdTimestamp":"2021-03-07T09:55:05.248+0000",
 * 	"createdBy":{
 * 		"userId":{
 * 			"space":"2021b.twins",
 * 			"email":"user2@demo.com"
 * 		}
 * 	},
 * 	"location":{
 * 		"lat":32.115139,
 * 		"lng":34.817804
 * 	},
 * "itemAttributes":{
 * 		"User":  {
 * 			"space": "2021b.twins",
 * 			"email": "abc@cba.com",
 * 		"Salary":"110"
 * 	}
 * }
 */

public class ItemBoundary {

	private ItemId itemId;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private User createdBy;
	private Map<String, Object> location;
	private Map<String, Object> itemAttributes;

	public ItemBoundary() {
		this.itemId = new ItemId();
		this.location = new HashMap<>();
		this.itemAttributes = new HashMap<>();
		this.createdTimestamp = new Date();
	}

	public ItemBoundary(ItemId itemId, String type, String name, Boolean active, User createdBy) {
		this();
		this.itemId = itemId;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdBy = createdBy;
	}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getLocation() {
		return location;
	}

	public void setLocation(Map<String, Object> location) {
		this.location = location;
	}

	public Map<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(Map<String, Object> itemAttributes) {

		this.itemAttributes = itemAttributes;
	}

	@Override
	public String toString() {
		return "ItemBoundary [itemId=" + itemId + ", type=" + type + ", name=" + name + ", active=" + active
				+ ", createdTimestamp=" + createdTimestamp + ", createdBy=" + createdBy + ", location=" + location
				+ ", itemAttributes=" + itemAttributes + "]";
	}

}
