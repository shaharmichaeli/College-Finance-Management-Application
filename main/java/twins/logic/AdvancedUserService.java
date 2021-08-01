package twins.logic;

import java.util.List;


//
public interface AdvancedUserService extends UserService{

	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int size, int page);

}
