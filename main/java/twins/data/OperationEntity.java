package twins.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
	OPERATION
--------------------
OPERATION_ID | OPERATION_SPACE | TYPE | ITEM_ID | ITEM_SPACE | CREATED_TIMES_STAMP | INVOKED_BY_EMAIL | INVOKED_BY_SPACE | OPERATION_ATTRIBUTES |
<PK>         |                 |	  |		    |			 |		               |			      |					 |                      |
*/

@Entity
@Table(name = "OPERATION")
public class OperationEntity {

	private String operationId;
	private String operationSpace;
	private String type;
	private String itemId;
	private String itemSpace;
	private Date createdTimestamp;
	private String invokedBySpace;
	private String invokedByEmail;
	private String operationAttributes;

	public OperationEntity() {
	}

	@Id
	public String getOperationId() {
		return operationId;
	}

	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	public String getOperationSpace() {
		return operationSpace;
	}

	public void setOperationSpace(String operationSpace) {
		this.operationSpace = operationSpace;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemSpace() {
		return itemSpace;
	}

	public void setItemSpace(String itemSpace) {
		this.itemSpace = itemSpace;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATION_CREATION")
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getInvokedByEmail() {
		return invokedByEmail;
	}

	public void setInvokedByEmail(String invokedByEmail) {
		this.invokedByEmail = invokedByEmail;
	}

	public String getInvokedBySpace() {
		return invokedBySpace;
	}

	public void setInvokedBySpace(String invokedBySpace) {
		this.invokedBySpace = invokedBySpace;
	}

	@Lob
	public String getOperationAttributes() {
		return operationAttributes;
	}

	public void setOperationAttributes(String operationAttributes) {
		this.operationAttributes = operationAttributes;
	}
}
