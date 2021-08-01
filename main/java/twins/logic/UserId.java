package twins.logic;

/* This class represent a UserId in the system.
 * space - The space the user exist, given by server.
 * email - Email provided by user, unique to each space.
 */
public class UserId {

	private String space;
	private String email;

	public UserId(String email, String space) {
		this.email = email;
		this.space = space;
	}

	public UserId() {

	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserId [space=" + this.space + ", email=" + this.email + "]";
	}
}
