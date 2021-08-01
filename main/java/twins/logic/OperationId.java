package twins.logic;

/* This class represent a operationId in the system.
 * space - The space the user exist, given by server.
 * id - The Id of the operation, unique to each space.
 */
public class OperationId {

	private String space;
	private String id;

	public OperationId() {
	}

	public OperationId(String space, String id) {
		this.space = space;
		this.id = id;
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
		return "OperationId [space=" + space + ", id=" + id + "]";
	}

}
