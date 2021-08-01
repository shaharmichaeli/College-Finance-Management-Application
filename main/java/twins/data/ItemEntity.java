package twins.data;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import twins.logic.User;

/*
 ITEMS
 --------------------------------------------------------
  ID   |   ITEM_ID  |  ITEM_SPACE  |  TYPE  |  NAME  |  ACTIVE  |  DATE  |  CREATED_BY  |  LOCATION  |  ITEM_ATRRIBUTES  |
 <PK>
 
 
 */
@Entity
@Table(name="ITEMS")
public class ItemEntity {
	private String Itemid;
	private String Itemspace;
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private String createdBy;
	private String location;
	private String itemAttributes;
	private String uniqueId;

	
	
	public ItemEntity() {
		// TODO Auto-generated constructor stub
	}
	/*
	public ItemEntity(String itemid, String itemspace, String type, String name, Boolean active,
			Date createdTimestamp, User createdBy, Map<String, Object> location, Map<String, Object> itemAttributes) {
		super();
		this.Itemid = itemid;
		this.Itemspace = itemspace;
		this.type = type;
		this.name = name;
		this.active = active;
		this.createdTimestamp = createdTimestamp;
		this.createdBy = createdBy;
		this.location = location;
		this.itemAttributes = itemAttributes;
		this.uniqueId = space + ":" + email;
	} */
	@Id
	public String getUniqueId() {
		return uniqueId;
	}
	
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	public String getItemid() {
		return Itemid;
	}
	
	public void setItemid(String itemid) {
		Itemid = itemid;
	}
	
	public String getItemspace() {
		return Itemspace;
	}
	
	public void setItemspace(String itemspace) {
		Itemspace = itemspace;
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
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ITEM_CREATION")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Lob
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	@Lob
	public String getItemAttributes() {
		return itemAttributes;
	}
	
	public void setItemAttributes(String itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
}
