package twins.logic;

/* NewUserDetails JavaBean..
 * 
 * Based on JSON format:
 * {
 * 		"email" : "",
 * 		"role" : "",
 * 		"username" :"",
 * 		"avatar" : ""
 * }
 */

public class NewUserDetails {
	private String email;
	private String role;
	private String username;
	private String avatar;

	public NewUserDetails() {
		//STUB - empty constructor
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
