package twins.logic;

public class User {

	private UserId userId;

	public User(String email, String space) {
		this.userId = new UserId(email, space);
	}

	public User() {

	}

	public UserId getUserId() {
		return userId;
	}
	public void setUserId(UserId userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + "]";
	}

}
