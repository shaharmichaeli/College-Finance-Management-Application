package twins.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import twins.data.UserEntity;

@Service
public class UserServiceImplementation implements AdvancedUserService {
	private UserDAO userDAO;
	private String space;

	@Value("${spring.application.name:dummy}")
	public void setSpace(String space) {
		this.space = space;
	}

	@PostConstruct
	public void postSpaceName() {
		System.err.println("UserServiceImplementation : Space (from configuration): " + this.space);
	}

	@Autowired
	public UserServiceImplementation(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserEntity boundaryToEntity(UserBoundary ub) {
		UserEntity entity = new UserEntity();
		entity.setUsername(ub.getUsername());
		entity.setEmail(ub.getUserId().getEmail());
		entity.setRole(ub.getRole());
		entity.setAvatar(ub.getAvatar());
		entity.setSpace(ub.getUserId().getSpace());
		entity.setUniqueId(ub.getUserId().getSpace()+":"+ub.getUserId().getEmail());
		return entity;
	}

	public UserBoundary entityToBoundary(UserEntity en) {
		UserBoundary ub = new UserBoundary();
		ub.setUserId(en.getEmail(), en.getSpace());
		ub.setUsername(en.getUsername());
		ub.setRole(en.getRole());
		ub.setAvatar(en.getAvatar());
		return ub;
	}

	public UserEntity entityToEntity(UserEntity en) {
		UserEntity entity = new UserEntity();
		entity.setUsername(en.getUsername());
		entity.setEmail(en.getEmail());
		entity.setRole(en.getRole());
		entity.setAvatar(en.getAvatar());
		entity.setSpace(en.getSpace());
		entity.setUniqueId(en.getSpace()+":"+en.getEmail());

		return entity;

	}

	// User Service Specification Methods.
	@Override
	@Transactional(readOnly = false)
	public UserBoundary createUser(NewUserDetails newUserDetails) {

		UserBoundary user = new UserBoundary();
		user.setUsername(newUserDetails.getUsername());
		user.setAvatar(newUserDetails.getAvatar());
		user.setRole(newUserDetails.getRole());
		user.setUserId(newUserDetails.getEmail(), this.space);


		Pattern pat = Pattern.compile( // Checks for email validation example@example.com
				"^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");

		if (!user.getRole().equals("ADMIN") && !user.getRole().equals("MANAGER") && !user.getRole().equals("PLAYER")) {
			throw new RuntimeException("UserServiceImplementation: createUser: Unacceptable Role in create user.");
		}

		if (user.getUsername() == null || user.getUsername() == "") {
			throw new RuntimeException("UserServiceImplementation: createUser: Unacceptable username in create user.");
		}

		if (user.getAvatar() == null || user.getAvatar().equals("")) {
			throw new RuntimeException("UserServiceImplementation: createUser: Unacceptable Avatar in create user.");
		}

		if (!pat.matcher(user.getUserId().getEmail()).matches()) {
			throw new RuntimeException("UserServiceImplementation: createUser: Unacceptable Email in create user.");
		}

		UserEntity en = boundaryToEntity(user);
		String uniqueId = en.getSpace() + ":" + en.getEmail();
		Optional<UserEntity> op = this.userDAO.findById(uniqueId);
		if (op.isPresent()) {
			throw new RuntimeException("UserServiceImplementation: createUser: This email already belongs to another user.");
		}
		en = userDAO.save(en);
		return entityToBoundary(en);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBoundary login(String userSpace, String userEmail) {
		if (userSpace == null || userEmail == null)
			throw new MessageNotFoundException("UserServiceImplementation: login: userSpace or userEmail is null");

		String uniqueId = userSpace + ":" + userEmail;
		Optional<UserEntity> op = this.userDAO.findById(uniqueId);
		if (op.isPresent()) {
			UserEntity tempEntity = op.get();
			return entityToBoundary(tempEntity);
		} else {
			throw new MessageNotFoundException("UserServiceImplementation: login: User not found.");
		}

	}

	@Override
	@Transactional(readOnly = false)
	public UserBoundary updateUser(String userSpace, String userEmail, UserBoundary update) {
		String uniqueId = userSpace + ":" + userEmail;
		Optional<UserEntity> op = this.userDAO.findById(uniqueId);
		if (op.isPresent()) {
			UserEntity exisiting = op.get();
			this.userDAO.delete(exisiting);

			UserEntity updated = boundaryToEntity(update);
			updated = this.userDAO.save(updated);
			return entityToBoundary(updated);
		} else {
			throw new MessageNotFoundException("UserServiceImplementation: updateUser: User not found.");
		}
	}

	@Deprecated
	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers() {
//		UserBoundary result = login(adminSpace, adminEmail);
//		if (result == null || !result.getRole().equals("ADMIN")) {
//			throw new RuntimeException(); // TODO RETURN STATUS 404
//		}
//
//		Iterable<UserEntity> allEntities = this.userDAO.findAll();
//
//		List<UserBoundary> rv = new ArrayList<>();
//		for (UserEntity entity : allEntities) {
//			UserBoundary boundary = entityToBoundary(entity);
//			rv.add(boundary);
//		}
//
//		return rv;
		throw new RuntimeException();

	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String adminSpace, String adminEmail, int page, int size) {
		UserBoundary result = login(adminSpace, adminEmail);
		if (result == null || !result.getRole().equals("ADMIN")) {
			throw new MessageNotFoundException("UserServiceImplementation: getAllUsers: Must be Admin for get all users.");
		}

		Page<UserEntity> pageOfUsers = this.userDAO.findAll(PageRequest.of(page, size, Direction.ASC, "uniqueId"));
		List<UserEntity> entities = pageOfUsers.getContent();
		List<UserBoundary> rv = new ArrayList<>();
		for (UserEntity entity : entities) {
			UserBoundary boundary = entityToBoundary(entity);
			rv.add(boundary);
		}

		return rv;

	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllUsers(String adminSpace, String adminEmail) {
		UserBoundary result = login(adminSpace, adminEmail);
		if (result == null || !result.getRole().equals("ADMIN")) {
			throw new MessageNotFoundException("UserController Error: Must be Admin for delete all.");
		}

		this.userDAO.deleteAll();

	}

}
