package twins.logic;

/* UserBoundary JavaBean..
 * 
 * Based on JSON format:
 * {
 * 		"userID": {
 * 			"space" : "",
 * 			"email" : "",
 * 	 	},
 * 		"role" : "",
 * 		"username" :"",
 * 		"avatar" : "",
 * }
 */
//


public class UserBoundary {
	private UserId userId;
	private String role;
	private String username;
	private String avatar;

//	public UserBoundary(String email, String role, String username, String avatar, String space) {
//		this.userId = new UserId(email, space);
//		this.role = role;
//		this.username = username;
//		this.avatar = avatar;
//	}

	public UserBoundary() {
		// STUB - Empty Constructor stub.
	}

	public UserId getUserId() {
		return userId;
	}

	public void setUserId(String email, String space) {
		this.userId = new UserId(email, space);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
