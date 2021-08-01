package twins.data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/*
 * USER
 * -------------------
 * USERNAME | EMAIL | ROLE | SPACE | AVATAR |
 * 			| <PK>  |      | <PK>  |        |		
 * 
 */

//
@Entity
@Table(name = "USERS")
public class UserEntity {

	private String username;
	private String role;
	private String avatar;
	private String space;
	private String email;
	private String uniqueId;

//	public UserEntity(String username, String email, String role, String avatar, String space) {
//		this.role = role;
//		this.username = username;
//		this.avatar = avatar;
//		this.email = email;
//		this.space = space;
//		this.uniqueId = space + ":" + email;
//	}

	public UserEntity() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	@Id
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
