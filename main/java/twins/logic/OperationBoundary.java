package twins.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* 
 * This class is used to transfer operation data. 
 * JSON Example:
 *{
 *	"operationId":{
 *		"space":"2021b.twins",
 *		"id":"451"
 *	},
 *	"type":"operationType",
 *	"item":{
 *		"itemId":{
 *		"space":"2021b.twins",
 *		"id":"99"
 *		}
 *	},
 *	"createdTimestamp":"2021-03-07T09:57:13.109+0000",
 *	"invokedBy":{
 *		"userId":{
 *			"space":"2021b.twins",
 *			"email":"user3@@demo.com"
 *		}
 *	},
 *	"operationAttributes":{
 *		"key1":"can be set to any value you wish",
 *		"key2":{
 *			"key2Subkey1":"can be nested json"
 *		}
 *	}
 *}
 */

public class OperationBoundary {

	private OperationId operationId;
	private String type;
	private Item item;
	private Date createdTimestamp;
	private User invokedBy;
	private Map<String, Object> operationAttributes;

	public OperationBoundary() {
		this.operationAttributes = new HashMap<>();
		this.createdTimestamp = new Date();
	}

	public OperationBoundary(OperationId operationId, String type, Item item, User invokedBy) {
		this();
		this.operationId = operationId;
		this.type = type;
		this.item = item;
		this.invokedBy = invokedBy;
	}

	public OperationId getOperationId() {
		return operationId;
	}

	public void setOperationId(OperationId operationId) {
		this.operationId = operationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public User getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(User invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(Map<String, Object> operationAttributes) {
		this.operationAttributes = operationAttributes;
	}

	@Override
	public String toString() {
		return "OperationBoundary [operationId=" + operationId + ", type=" + type + ", item=" + item
				+ ", createdTimestamp=" + createdTimestamp + ", invokedBy=" + invokedBy + ", operationAttributes="
				+ operationAttributes + "]";
	}
}
