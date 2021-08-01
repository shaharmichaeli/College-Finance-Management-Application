package twins.logic;

import java.util.List;

public interface UserService {
	public UserBoundary createUser(NewUserDetails user);

	public UserBoundary login(String userSpace, String userEmail);

	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update);

	public List<UserBoundary> getAllUsers();

	public void deleteAllUsers(String adminSpace, String adminEmail);

}
