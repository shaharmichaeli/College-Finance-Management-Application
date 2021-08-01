package twins.logic;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class UserController {
	private AdvancedUserService userLogic;
	private String space;

	@Autowired
	public void setUserLogic(AdvancedUserService userLogic) {
		this.userLogic = userLogic;
	}
	// User API actions.

	@RequestMapping(path = "/twins/users", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public UserBoundary createNewUser(@RequestBody NewUserDetails newUserDetails) {
		return this.userLogic.createUser(newUserDetails);
	}

	@RequestMapping(path = "/twins/users/login/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

	public UserBoundary loginValidUserAndReturnUserDetails(@PathVariable("userSpace") String space,
			@PathVariable("userEmail") String email) {
		return this.userLogic.login(space, email);
	}

	@RequestMapping(path = "/twins/users/{userSpace}/{userEmail}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void updateUserDetails(@PathVariable("userEmail") String email, @PathVariable("userSpace") String space,
			@RequestBody UserBoundary ub) {
		this.userLogic.updateUser(space, email, ub);

	}

	// Admin API actions

	@RequestMapping(path = "/twins/admin/users/{userSpace}/{userEmail}", method = RequestMethod.DELETE)

	public void deleteAllUsersInTheSpace(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		this.userLogic.deleteAllUsers(userSpace, userEmail);
	}

	@RequestMapping(path = "/twins/admin/users/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UserBoundary> exportAllUsers(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(required = false, defaultValue = "0", name = "page") int page,
			@RequestParam(required = false, defaultValue = "5", name = "size") int size) {
		
		List<UserBoundary> boundaries = this.userLogic.getAllUsers(userSpace, userEmail, page, size);
		return boundaries;
	}

	@Value("${spring.application.name:dummy}")
	public void setSpace(String space) {
		this.space = space;
	}

	@PostConstruct
	public void postSpaceName() {
		System.err.println("Controller : Space (from configuration): " + this.space);
	}

}
